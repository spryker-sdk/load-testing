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

trait CheckoutWithFiftyToSeventyItemsApiBase {

  lazy val scenarioName = "Checkout With Fifty To Seventy Items Api"

  val httpProtocol = GlueProtocol.httpProtocol
  val productFeeder = csv("tests/_data/product_concrete.csv").random
  val customerFeeder = csv("tests/_data/customer.csv").random

  val feeder = Iterator.continually(Map(
  "customerEmail" -> (Random.alphanumeric.take(30).mkString + "@gmail.com"), 
  "password" -> "supe!rsEcu1re", 
  "cartItemsQuantityFirstChunk" -> 30,
  "cartItemsQuantitySecondChunk" -> 30))

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
    .check(jsonPath("$.data.id").saveAs("cart_id"))

  val addMultipleItemsToCartRequestFirstChunk = http("Add Multiple Items to Cart Request first chunk")
    .post("/carts/${cart_id}/fixtures")
    .body(StringBody("""{"data":{"type":"fixtures","attributes":{"itemsQuantity":"${cartItemsQuantityFirstChunk}","sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val addMultipleItemsToCartRequestSecondChunk = http("Add Multiple Items to Cart Request second chunk")
    .post("/carts/${cart_id}/fixtures")
    .body(StringBody("""{"data":{"type":"fixtures","attributes":{"itemsQuantity":"${cartItemsQuantitySecondChunk}","sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val checkoutRequest = http("Checkout Request")
    .post("/checkout")
    .body(StringBody("""{"data":{"type":"checkout","attributes":{"customer":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"${customerEmail}","dateOfBirth":"1957-10-23","phone":"1 800-123-0000"},"idCart":"${cart_id}","billingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"payments":[{"paymentProviderName":"DummyPayment","paymentMethodName":"Invoice"}],"shipment":{"idShipmentMethod":3},"shipments":[{"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"items":["string"],"idShipmentMethod":3,"requestedDeliveryDate":"2022-09-19"}],"cartNote":"string"}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .repeat(1) {
       exec(createCustomerRequest)
      .pause(5)
      .exec(accessTokenRequest)
      .pause(5)
      .exec(createCartRequest)
      .pause(5)
      .exec(addMultipleItemsToCartRequestFirstChunk)
      .pause(5)
      .exec(addMultipleItemsToCartRequestSecondChunk)
      .pause(5)
      .exec(checkoutRequest)
      .pause(5)
    }
}

class CheckoutWithFiftyToSeventyItemsApiRamp extends Simulation with CheckoutWithFiftyToSeventyItemsApiBase {

  override lazy val scenarioName = "Checkout With Fifty To Seventy Items Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
}

class CheckoutWithFiftyToSeventyItemsApiSteady extends Simulation with CheckoutWithFiftyToSeventyItemsApiBase {

  override lazy val scenarioName = "Checkout With Fifty To Seventy Items Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
