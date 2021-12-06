/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random
import spryker.GlueProtocol._
import spryker.Scenario._

trait ProductOffersForProductsConcreteApiBase {

  lazy val scenarioName = "Product Offers For Products Concrete Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/concrete-products/041498248932/product-offers?include=concrete-products,concrete-product-image-sets,product-offer-prices")
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJmcm9udGVuZCIsImp0aSI6ImVkZmNlMjI4NDY0OWE3NTczMGQyM2M1ZjUyN2U2OGM2Njk3MWM1NGVlYjJmMDJiMGY3ZGU0MTQyMDcwZGI3NjJkNGI5MWVlMDg2NmIxNTRmIiwiaWF0IjoxNjMxNTMzNjQ4LjMwNjc0MiwibmJmIjoxNjMxNTMzNjQ4LjMwNjc2NTEsImV4cCI6MTYzMTU2MjQ0OC4yNzAxODksInN1YiI6IntcImlkX2FnZW50XCI6bnVsbCxcImN1c3RvbWVyX3JlZmVyZW5jZVwiOlwiREUtLTNcIixcImlkX2N1c3RvbWVyXCI6M30iLCJzY29wZXMiOlsiY3VzdG9tZXIiXX0.tPGLrpi9WgH0q8JA0vjElxMs1D81M65eW9wUeaY-fx4gIHzTRyzh5tH-FKu4vS0r9VoUKITf-LuXE4Ydn2zUiwCcX59PQGGLegzrzFmgQcXFrqzPLyT1fIhsFMCfao-XhO5ziAbsh0nG6wyxG3cpO0oLygR1uNk-I1dzur-GIj5LKJmcJioQJHH1bw-XDGgbKoomcRHNgN7902WZQcar3gR9Z5HhqfMwjVLnVeWerVJ7fbdSRgyr3mEQIVNHrvcgljl7nm1hQEJhwShYDx13x7J5sAoItkw09m5OgsUCQJHsFqwYUntq3HtApPXWLYXQDu2L2kg5TSdx-311pVOp2g")
    .header("Merchant-Reference", "474-001")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(request)
}

class ProductOffersForProductsConcreteApiRamp extends Simulation with ProductOffersForProductsConcreteApiBase {

  override lazy val scenarioName = "Product Offers For Products Concrete API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ProductOffersForProductsConcreteApiSteady extends Simulation with ProductOffersForProductsConcreteApiBase {

  override lazy val scenarioName = "Product Offers For Products Concrete API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
