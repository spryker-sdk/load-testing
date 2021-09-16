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
import spryker.Scenario._

trait AddItemToCartApiBase {

  lazy val scenarioName = "Add Item To Cart Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val addToCartRequest = http("Add Item To Cart Api")
    .post("/carts/13f5aabc-48d3-5da6-a591-cce470de39a1/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":2},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJmcm9udGVuZCIsImp0aSI6IjQ4MjBiZTM3MzU5NmY1NDdmZmVhMTNjZDkzZmRiYmQxYTA1N2E0MWFlNzIyYWRkN2UyMzU3NTA4NzA0OThjMWMzOWIwMjY5YTFjMDdmM2JlIiwiaWF0IjoxNjMxNzgwMDMxLjA0OTE4LCJuYmYiOjE2MzE3ODAwMzEuMDQ5MTg1LCJleHAiOjE2MzE4MDg4MzAuOTU4NzAwOSwic3ViIjoie1wiaWRfYWdlbnRcIjpudWxsLFwiY3VzdG9tZXJfcmVmZXJlbmNlXCI6XCJVUy1RQS0yMlwiLFwiaWRfY3VzdG9tZXJcIjoyMn0iLCJzY29wZXMiOlsiY3VzdG9tZXIiXX0.Esx23uBhIXMFz-aVbmvBVhcbiVqo4CFVD7vURP_11tLfphnEYppgFzzhxraB1XAXKHiBwMbRkuWFwkcl1X501h7mXvmxE-G22tXC3imYopkXjuN9f9QlD5Axh2yqRaKL7qHoINXJEa-vUGdMaUi_v_A1fCSCeKwx88lRmQSpZxK0q2RFlyArT96VDZn-nM1OVQp9OPedt-Eit0lw2TKGDy1lXQsqGu9oL6qqQLkKQpRFQumPhTOoUctfmv_nBkE3PFqC4dnax-txIzVr4usFZK3pZt1GTrKSpiZFovShDDqlCi17g0IjuIxRu-xfO5HzhOl-hLVkK_UvYGzVGCAauQ")
    .check(status.is(201))

  val scn = scenario(scenarioName)
      .exec(addToCartRequest)
}

class AddItemToCartApiRamp extends Simulation with AddItemToCartApiBase {

  override lazy val scenarioName = "Add Item To Cart Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class AddItemToCartApiSteady extends Simulation with AddItemToCartApiBase {

  override lazy val scenarioName = "Add Item To Cart Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
