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
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCheckoutRequestApi._

trait GetOrdersFrontendApiBase {
  lazy val scenarioName = "Retrieves order by id."

  val httpProtocol = GlueProtocol.httpProtocol

    val request = http(scenarioName)
    .get("/orders/${order_reference}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))
  
  val scn = scenario(scenarioName)
    .exec(CreateCheckoutRequestApi.executeRequest)
    .exec(request)
}

class GetOrdersFrontendApiRamp extends Simulation with GetOrdersFrontendApiBase {

  override lazy val scenarioName = "Retrieves order by id. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetOrdersFrontendApiSteady extends Simulation with GetOrdersFrontendApiBase {

  override lazy val scenarioName = "Retrieves order by id. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
