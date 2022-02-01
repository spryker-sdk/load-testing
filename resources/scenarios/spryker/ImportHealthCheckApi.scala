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
import io.netty.handler.codec.http._
import io.netty.util.CharsetUtil
import java.nio.charset.Charset
import io.gatling.core.akka._
import scala.concurrent.duration._
import spryker.BackendApiProtocol._

trait ImportHealthCheckApiBase {

  lazy val scenarioName = "Import Health Check Api"

  val httpProtocol = BackendApiProtocol.httpProtocol

  var request = http(scenarioName)
    .post("/import-health_check")
    .check(status.is(200))

  if (!BackendApiProtocol.instanceName.isEmpty &&
      !BackendApiProtocol.basicAuthUsername.isEmpty &&
      !BackendApiProtocol.basicAuthPassword.isEmpty) {
      request = request.basicAuth(BackendApiProtocol.basicAuthUsername, BackendApiProtocol.basicAuthPassword)
  }

  val scn = scenario(scenarioName)
  .exec(request)
}

class ImportHealthCheckApiRamp extends Simulation with ImportHealthCheckApiBase {

  override lazy val scenarioName = "Import Health Check Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ImportHealthCheckApiSteady extends Simulation with ImportHealthCheckApiBase {

  override lazy val scenarioName = "Import Health Check Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
