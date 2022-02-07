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
import java.util.concurrent.atomic.{AtomicInteger}

trait GetOrderDetailPageZedApiBase {

  lazy val scenarioName = "Import Merchants Api"

  val httpProtocol = ZedApiProtocol.httpProtocol

  val id = new AtomicInteger(0)
  val feeder = Iterator.continually(Map("ordersStart" -> id.getAndIncrement()))

    var csrfTokenRequest = http("Get CSRF token")
    .get("/security-gui/login")
    .check(css("#auth__token", "value").saveAs("csrf_token"))
    .check(status.is(200))

    var authRequest = http("Authorize")
    .post("/login_check")
    .formParam("auth[username]", ZedApiProtocol.backofficeAuthUsername)
    .formParam("auth[password]", ZedApiProtocol.backofficeAuthPassword)
    .formParam("auth[_token]", "${csrf_token}")
    .check(header("Set-Cookie").saveAs("cookie_for_auth"))
    .check(status.is(200)) 

  var ordersRequest = http("Get table of orders from Zed API")
    .get("/sales/index/table?start=${ordersStart}&length=1")
    .header("Cookie", session => session("cookie_for_auth").as[String])
    .check(jsonPath("$.data[0][1]").saveAs("order_id"))
    .check(status.is(200))

  var request = http("Get order detail page Zed API")
    .get("/sales/detail?id-sales-order=${order_id}")
    .check(bodyString.saveAs("ordersDetailRequestResponse"))
    .check(status.is(200))

  val scn = scenario(scenarioName)
  .feed(feeder)
  .exec(csrfTokenRequest)
  .exec(authRequest)
  .exec(ordersRequest)
  .exec(request)
}

class GetOrderDetailPageZedApiRamp extends Simulation with GetOrderDetailPageZedApiBase {

  override lazy val scenarioName = "Get order detail page Zed API [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetOrderDetailPageZedApiSteady extends Simulation with GetOrderDetailPageZedApiBase {

  override lazy val scenarioName = "Get order detail page Zed API [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
