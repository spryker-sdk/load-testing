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
import spryker.CreateCheckoutRequestSeventyItemsApi._
import spryker.Scenario._
import io.gatling.core.feeder._

trait CreateCheckoutSeventyItemsFrontendApiBase {

lazy val scenarioName = "Checkout with 70 items Full Flow Api"

val scn = scenario(scenarioName)
  .exec(CreateCheckoutRequestSeventyItemsApi.executeRequest)
}

class CreateCheckoutSeventyItemsFrontendApiRamp extends Simulation with CreateCheckoutSeventyItemsFrontendApiBase {

  override lazy val scenarioName = "Checkout with 70 items Full Flow Api [Incremental]"

  setUp(scn.inject(       
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (30), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCheckoutSeventyItemsFrontendApiSteady extends Simulation with CreateCheckoutSeventyItemsFrontendApiBase {

  override lazy val scenarioName = "Checkout with 70 items Full Flow Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
