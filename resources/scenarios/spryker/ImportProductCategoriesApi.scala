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

trait ImportProductCategoriesApiBase {

  lazy val scenarioName = "Import Product Categories Api"

  val httpProtocol = BackendApiProtocol.httpProtocol

  val basicAuthUsername = sys.env.getOrElse("BACKEND_API_USERNAME", "").toString
  val basicAuthPassword = sys.env.getOrElse("BACKEND_API_PASSWORD", "").toString

  val feeder = csv("tests/_data/category_product_bind_bodies.csv").circular

  val addToCartRequest = http("Import Product Categories Api")
    .post("/import-product_categories")
    .body(StringBody("""${payload}""")).asJson
    .basicAuth(basicAuthUsername, basicAuthPassword)
    .check(status.is(200))

  val scn = scenario(scenarioName)
  .feed(feeder)
  .exec(addToCartRequest)
}

class ImportProductCategoriesApiRamp extends Simulation with ImportProductCategoriesApiBase {

  override lazy val scenarioName = "Import Product Categories Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class ImportProductCategoriesApiSteady extends Simulation with ImportProductCategoriesApiBase {

  override lazy val scenarioName = "Import Product Categories Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
