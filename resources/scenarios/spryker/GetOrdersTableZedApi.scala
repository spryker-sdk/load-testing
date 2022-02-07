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
import spryker.ZedApiProtocol._

trait GetOrdersTableZedApiBase {

  lazy val scenarioName = "Import Merchants Api"

  val httpProtocol = ZedApiProtocol.httpProtocol

  var request = http("Get table of orders from Zed API")
    .get("/sales/index/table")
    .check(status.is(200))

  if (!ZedApiProtocol.instanceName.isEmpty &&
      !ZedApiProtocol.basicAuthUsername.isEmpty &&
      !ZedApiProtocol.basicAuthPassword.isEmpty) {
      request = request.basicAuth(ZedApiProtocol.basicAuthUsername, ZedApiProtocol.basicAuthPassword)
  } 

  val scn = scenario(scenarioName)
  .exec(session => {
    println(session("id").as[String])
    session
  })
  .exec(request)
  
}

class GetOrdersTableZedApiRamp extends Simulation with GetOrdersTableZedApiBase {

  override lazy val scenarioName = "Get table of orders from Zed API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetOrdersTableZedApiSteady extends Simulation with GetOrdersTableZedApiBase {

  override lazy val scenarioName = "Get table of orders from Zed API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
