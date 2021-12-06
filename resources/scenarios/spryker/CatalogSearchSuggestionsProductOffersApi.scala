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

trait CatalogSearchSuggestionsProductOffersApiBase {

  lazy val scenarioName = "Catalog Search Suggestions Product Offers Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val groceryItemsFeeder = csv("tests/_data/grocery_items.csv").random
  val merchantsFeeder = csv("tests/_data/merchants.csv").random

  val request = http(scenarioName)
    .get("/catalog-search-suggestions-product-offers?q=${name}")
    .header("Merchant-Reference", "${merchant_reference}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
  .feed(groceryItemsFeeder)
  .feed(merchantsFeeder)
  .exec(request)
}

class CatalogSearchSuggestionsProductOffersApiRamp extends Simulation with CatalogSearchSuggestionsProductOffersApiBase {

  override lazy val scenarioName = "Catalog Search Suggestions Product Offers API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CatalogSearchSuggestionsProductOffersApiSteady extends Simulation with CatalogSearchSuggestionsProductOffersApiBase {

  override lazy val scenarioName = "Catalog Search Suggestions Product Offers API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
