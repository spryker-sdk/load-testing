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

trait CheckoutApiBase {

  lazy val scenarioName = "Checkout Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val checkoutRequest = http("Checkout Request")
    .post("/checkout-testing")
    .body(StringBody("""{"data":{"type":"checkout-testing","attributes":{"itemsQuantity":1,"customer":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"zingeon1@gmail.com","dateOfBirth":"1957-10-23","phone":"1 800-123-0000"},"idCart":"test","billingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"payments":[{"paymentProviderName":"DummyPayment","paymentMethodName":"Invoice"}],"shipment":{"idShipmentMethod":3},"shipments":[{"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"items":["string"],"idShipmentMethod":3,"requestedDeliveryDate":"2022-09-19"}],"cartNote":"string"}}}""")).asJson
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
      .exec(checkoutRequest)
}

class CheckoutApiRamp extends Simulation with CheckoutApiBase {

  override lazy val scenarioName = "Checkout Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class CheckoutApiSteady extends Simulation with CheckoutApiBase {

  override lazy val scenarioName = "Checkout Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
