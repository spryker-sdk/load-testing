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
import io.gatling.core.feeder._
import spryker.CreateCustomerRequestApi._
import spryker.CreateAccessTokenRequestApi._
import spryker.CreateCartRequestApi._
import spryker.AddToCartRequestApi._

trait CreateCartMerchantTimeslotReservationsFrontendApiBase {

  lazy val scenarioName = "Reserve pre-selected timeslot from a cart."

  val productConcreteFeeder = csv("tests/_data/product_concrete.csv").random

  val feeder = Iterator.continually(Map(
    "customer_email" -> (Random.alphanumeric.take(30).mkString + "@gmail.com"), 
    "password" -> "supe!rsEcu1re"
  ))

  val createCustomerRequest = http("Create Customer Request")
    .post("/customers")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"customers","attributes":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"${customer_email}","password":"${password}","confirmPassword":"${password}","acceptedTerms":true}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.id").saveAs("customer_id"))

  val accessTokenRequest = http("Access Token Request")
    .post("/access-tokens")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"access-tokens","attributes":{"username":"${customer_email}","password":"${password}"}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.accessToken").saveAs("access_token"))

  val createCartRequest = http("Create Cart Request")
    .post("/carts")
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .body(StringBody("""{"data":{"type":"carts","attributes":{"priceMode":"NET_MODE","currency":"USD","store":"US","merchantReference":"${merchant_reference}","merchantSelections":{"serviceType":"pickup","merchantFilterAddress":{"address1":"Kharkiv Ukraine","zipCode":"12345"}},"merchantTimeslot":{"merchantTimeslotId":"${merchant_timeslot_id}","startTime":"2022-09-16","endTime":"2022-12-18","merchantTimeslotReservation":{"merchantTimeslotReservationId":1,"expirationDate":"2022:03:01-14:37:45"}}}}}""")).asJson
    .check(status.is(201))
    .check(jsonPath("$.data.id").saveAs("cart_id"))
    .check(header("ETag").saveAs("if_match_header"))

  val previewPickupTimeslotsRequest = http("Retrieves list of merchant pickup timeslots for preview.")
    .get("/preview-pickup-timeslots?merchantReference=${merchant_reference}")
    .check(jsonPath("$.data[1].id").saveAs("merchant_timeslot_id"))
    .check(status.is(200))

  val merchantTimeslotReservationsRequest = http("Reserve pre-selected timeslot from a cart.")
    .post("/carts/${cart_id}/merchant-timeslot-reservations")
    .body(StringBody("""{"data":{"type":"merchant-timeslot-reservations","attributes":{}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("If-Match", "${if_match_header}")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .feed(productConcreteFeeder)
    .exec(createCustomerRequest)
    .exec(accessTokenRequest)
    .exec(previewPickupTimeslotsRequest)
    .exec(createCartRequest)
    .exec(merchantTimeslotReservationsRequest)
}

class CreateCartMerchantTimeslotReservationsFrontendApiRamp extends Simulation with CreateCartMerchantTimeslotReservationsFrontendApiBase {

  override lazy val scenarioName = "Reserve pre-selected timeslot from a cart. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCartMerchantTimeslotReservationsFrontendApiSteady extends Simulation with CreateCartMerchantTimeslotReservationsFrontendApiBase {

  override lazy val scenarioName = "Reserve pre-selected timeslot from a cart. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}