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
import java.net.URL

trait ResultsBase {

  lazy val scenarioName = "Results page"

  val httpProtocol = YvesProtocol.httpProtocol
  val url = new URL(YvesProtocol.baseUrl)
  val hostName = url.getHost

  val request = http(scenarioName)
    .get("/results")
    .header("Authorization", "Basic YWxkaTpsaWtlc2Nsb3Vk")
    .header("Host", hostName)
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(request)
}

class ResultsRamp extends Simulation with ResultsBase {

  override lazy val scenarioName = "Results page [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ResultsSteady extends Simulation with ResultsBase {

  override lazy val scenarioName = "Results page [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}