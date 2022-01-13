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

trait CartsFullFlowApiBase {

  lazy val scenarioName = "Carts Full Flow Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val feeder = Iterator.continually(Map("customerEmail" -> (Random.alphanumeric.take(30).mkString + "@gmail.com"), "password" -> "supe!rsEcu1re"))

  val createCustomerRequest = http("Create Customer Request")
    .post("/customers")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"customers","attributes":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"${customerEmail}","password":"${password}","confirmPassword":"${password}","acceptedTerms":true}}}""")).asJson
    .check(status.is(201))

  val accessTokenRequest = http("Access Token Request")
    .post("/access-tokens")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"access-tokens","attributes":{"username":"${customerEmail}","password":"${password}"}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.accessToken").saveAs("access_token"))

  val createCartRequest = http("Create Cart Request")
    .post("/carts")
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"carts","attributes":{"priceMode":"NET_MODE","currency":"USD","store":"US","merchantReference":"474-001","merchantSelections":{"serviceType":"delivery","merchantFilterAddress":{"address1":"Kharkiv Ukraine","zipCode":"12345"}},"merchantTimeslot":{"merchantTimeslotId":1,"startTime":"2022-09-16","endTime":"2022-12-18","merchantTimeslotReservation":{"merchantTimeslotReservationId":1,"expirationDate":"2022:03:01-14:37:45"}}}}}""")).asJson
    .check(status.is(201))

  val getCartsRequest = http("Get Carts Request")
    .get("/carts")
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(feeder)
      .exec(createCustomerRequest)
      .pause(3)
      .exec(accessTokenRequest)
      .pause(3)
      .exec(createCartRequest)
      .pause(3)
      .exec(getCartsRequest)
}

class CartsFullFlowApiRamp extends Simulation with CartsFullFlowApiBase {

  override lazy val scenarioName = "Carts Full Flow Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CartsFullFlowApiSteady extends Simulation with CartsFullFlowApiBase {

  override lazy val scenarioName = "Carts Full Flow Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}