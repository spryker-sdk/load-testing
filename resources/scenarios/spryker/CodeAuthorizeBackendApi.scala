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
import spryker.BackendApiProtocol._
import spryker.Scenario._

trait CodeAuthorizeBackendApiBase {

  lazy val scenarioName = "Code Authorize Backend Api"

  val httpProtocol = BackendApiProtocol.httpProtocol
  val usersFeeder = csv("tests/_data/users.csv").random

  val codeAuthorizeRequest = http(scenarioName)
    .post("/code-authorize")
    .formParam("username", "${user_email}")
    .formParam("password", "${user_password}")
    .formParam("grantType", "authorization_code_backend")
    .formParam("response_type", "code")
    .formParam("client_id", "picking_api")
    .formParam("code_challenge_method", "S256")
    .formParam("code_challenge", "YLElACP9cTwgsCUc1UdGUnes1VNwSmtK1rMC0GfZMK4")
    .formParam("redirect_uri", "http://url/")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(usersFeeder)
    .exec(codeAuthorizeRequest)
}

class CodeAuthorizeBackendApiRamp extends Simulation with CodeAuthorizeBackendApiBase {

  override lazy val scenarioName = "Code Authorize Backend Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class CodeAuthorizeBackendApiSteady extends Simulation with CodeAuthorizeBackendApiBase {

  override lazy val scenarioName = "Code Authorize Backend Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
