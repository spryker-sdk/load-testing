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
import spryker.BackendApiProtocol._
import spryker.Scenario._

trait PickingOrdersBackendApiBase {

  lazy val scenarioName = "Picking Orders Backend Api"

  val httpProtocol = BackendApiProtocol.httpProtocol
  val glueBaseUrl = GlueProtocol.baseUrl
  val customerFeeder = csv("tests/_data/customer.csv").random
  val merchantsFeeder = csv("tests/_data/merchants.csv").random

  val accessTokenRequest = http("Access Token Request")
    .post(glueBaseUrl + "/access-tokens")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"access-tokens","attributes":{"username":"${email}","password":"${password}"}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.accessToken").saveAs("access_token"))

  val pickingOrdersRequest = http(scenarioName)
    .get("/merchants/${merchant_reference}/picking-orders")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(customerFeeder)
    .feed(merchantsFeeder)
    .exec(accessTokenRequest)
    .exec(pickingOrdersRequest)
}

class PickingOrdersBackendApiRamp extends Simulation with PickingOrdersBackendApiBase {

  override lazy val scenarioName = "Picking Orders Backend Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class PickingOrdersBackendApiSteady extends Simulation with PickingOrdersBackendApiBase {

  override lazy val scenarioName = "Picking Orders Backend Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
