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

trait GuestCartApiBase {

  lazy val scenarioName = "Add to Guest Cart Api"

  val httpProtocol = GlueProtocol.httpProtocol
  val productFeeder = csv("tests/_data/product_concrete.csv").random
  val uniqueIdFeeder = Iterator.continually(Map("uniqueId" -> (Random.alphanumeric.take(25).mkString)))

  val addToCartRequest = http("Add to Guest Cart")
    .post("/guest-cart-items")
    .body(StringBody("""{"data": {"type": "guest-cart-items", "attributes": {"sku": "${sku}", "quantity": 1}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${uniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(201))
    .check(jsonPath("$.data.id").saveAs("cart_uuid"))

  val scn = scenario(scenarioName)
    .feed(uniqueIdFeeder)
    .repeat(1) {
      feed(productFeeder)
      .exec(addToCartRequest)
    }
}

class GuestCartApiRamp extends Simulation with GuestCartApiBase {

  override lazy val scenarioName = "Add to Guest Cart Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class GuestCartApiSteady extends Simulation with GuestCartApiBase {

  override lazy val scenarioName = "Add to Guest Cart Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
