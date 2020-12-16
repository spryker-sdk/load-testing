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

trait AddToCustomerCartBase {

  lazy val scenarioName = "Add To Customer Cart"

  val httpProtocol = YvesProtocol.httpProtocol
  val productFeeder = csv("tests/_data/product_concrete.csv").random
  val customerFeeder = csv("tests/_data/customer.csv").random

  val loginPageRequest = http("Login Page Request")
    .get("/en/login")
    .check(status.is(200))
    .check(css("input[name=\"loginForm[_token]\"]", "value").saveAs("loginFormCsrfToken"))

  val loginRequest = http("Login Request")
    .post("/login_check")
    .formParam("loginForm[email]", "${email}")
    .formParam("loginForm[password]", "${password}")
    .formParam("loginForm[_token]", "${loginFormCsrfToken}")


  val pdpRequest = http("PDP Reqest")
    .get("${pdp_url}")
    .check(status.is(200))
    .check(css("input[name=\"add_to_cart_form[_token]\"]", "value").saveAs("addToCartFormCsrfToken"))

  val request = http(scenarioName)
    .post("/en/cart/add/${sku}")
    .formParam("quantity", "1")
    .formParam("add_to_cart_form[_token]", "${addToCartFormCsrfToken}")

  val scn = scenario(scenarioName)
    .feed(productFeeder)
    .repeat(1) {
      feed(customerFeeder)
      .exec(loginPageRequest)
      .pause(1)
      .exec(loginRequest)
      .pause(1)
      .exec(pdpRequest)
      .pause(1)
      .exec(request)
    }

}

class AddToCustomerCartRamp extends Simulation with AddToCustomerCartBase {

  override lazy val scenarioName = "Add To Customer Cart [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class AddToCustomerCartSteady extends Simulation with AddToCustomerCartBase {

  override lazy val scenarioName = "Add To Customer Cart [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
