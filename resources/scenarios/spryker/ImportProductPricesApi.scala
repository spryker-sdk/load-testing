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
import spryker.GlueProtocol._

trait ImportProductPricesApiBase {

  lazy val scenarioName = "Import Product Prices Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val basicAuthUsername = sys.env.getOrElse("BACKEND_API_USERNAME", "").toString
  val basicAuthPassword = sys.env.getOrElse("BACKEND_API_PASSWORD", "").toString

  val feeder = csv("tests/_data/product_price_bodies.csv").random

  val addToCartRequest = http("Import Product Prices Api")
    .post("/import-product_prices")
    .body(StringBody("""${payload}""")).asJson
    .basicAuth(basicAuthUsername, basicAuthPassword)
    .check(status.is(200))

  val scn = scenario(scenarioName)
  .feed(feeder)
  .exec(addToCartRequest)
}

class ImportProductPricesApiRamp extends Simulation with ImportProductPricesApiBase {

  override lazy val scenarioName = "Import Product Prices Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ImportProductPricesApiSteady extends Simulation with ImportProductPricesApiBase {

  override lazy val scenarioName = "Import ProductPrices Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
