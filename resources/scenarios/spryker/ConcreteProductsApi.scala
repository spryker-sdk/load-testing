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

trait ConcreteProductsApiBase {

  lazy val scenarioName = "Concrete Products Api"

  val httpProtocol = GlueProtocol.httpProtocol
  val feeder = csv("tests/_data/product_concrete.csv").random

  val request = http(scenarioName)
    .get("/concrete-products/${concreteProductReference}/product-offers?include=concrete-products,concrete-product-image-sets,product-offer-prices")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(request)
}

class ConcreteProductsApiRamp extends Simulation with ConcreteProductsApiBase {

  override lazy val scenarioName = "Concrete Products API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ConcreteProductsApiSteady extends Simulation with ConcreteProductsApiBase {

  override lazy val scenarioName = "Concrete Products API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
