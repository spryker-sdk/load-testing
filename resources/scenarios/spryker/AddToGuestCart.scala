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
import spryker.YvesProtocol._
import spryker.Scenario._

trait AddToGuestCartBase {

  lazy val scenarioName = "Add To Guest Cart"

  val httpProtocol = YvesProtocol.httpProtocol
  val feeder = csv("tests/_data/product_concrete.csv").random

  val pdpRequest = http("PDP Reqest")
    .get("${pdp_url}")
    .check(status.is(200))
    .check(css("input[name=\"add_to_cart_form[_token]\"]", "value").saveAs("csrfToken"))

  val addToCartRequest = http(scenarioName)
    .post("/en/cart/add/${sku}")
    .formParam("quantity", "1")
    .header("Cookie", "XDEBUG_SESSION=XDEBUG_ECLIPSE")
    .formParam("add_to_cart_form[_token]", "${csrfToken}")

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(pdpRequest)
    .exec(addToCartRequest)
}

class AddToGuestCartRamp extends Simulation with AddToGuestCartBase {

  override lazy val scenarioName = "Add To Guest Cart [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class AddToGuestCartSteady extends Simulation with AddToGuestCartBase {

  override lazy val scenarioName = "Add To Guest Cart [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
