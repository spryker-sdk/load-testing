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

trait GetProductsBackendApiBase {

  lazy val scenarioName = "Get Products Backend Api"

  val httpProtocol = BackendApiProtocol.httpProtocol
  val glueBaseUrl = GlueProtocol.baseUrl

  val customerFeeder = csv("tests/_data/customer.csv").random
  val usersFeeder = csv("tests/_data/users.csv").random
  val merchantsFeeder = csv("tests/_data/merchants.csv").random
  val productFeeder = Iterator.continually(Map("product_id" -> (1 + Random.nextInt(10))))


  val accessTokenRequest = http("Access Token Request")
    .post(glueBaseUrl + "/access-tokens")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"access-tokens","attributes":{"username":"${email}","password":"${password}"}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.accessToken").saveAs("access_token"))

  val getProductsRequest = http(scenarioName)
    .get("/merchants/${merchant_reference}/products?filter[product.ids]=${product_id}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(customerFeeder)
    .feed(usersFeeder)
    .feed(productFeeder)
    .feed(merchantsFeeder)
    .exec(accessTokenRequest)
    .exec(getProductsRequest)
}

class GetProductsBackendApiRamp extends Simulation with GetProductsBackendApiBase {

  override lazy val scenarioName = "Get Products Backend Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetProductsBackendApiSteady extends Simulation with GetProductsBackendApiBase {

  override lazy val scenarioName = "Get Products Backend Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
