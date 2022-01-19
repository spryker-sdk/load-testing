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

trait CheckoutWithOffersApiBase {

    lazy val scenarioName = "Checkout With Offers Api"

    val httpProtocol = GlueProtocol.httpProtocol
    val productFeeder = csv("tests/_data/merchant_product_offer.csv").random
    val customerFeeder = csv("tests/_data/customer.csv").random

    val getAccessTokenRequest = http("Get Access Token")
        .post("/access-tokens")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"data": {"type": "access-tokens", "attributes": {"username": "${email}", "password": "${password}"}}}"""))
        .check(status.is(201))
        .check(jsonPath("$.data.attributes.accessToken").saveAs("auth_token"))

    val getCartRequest = http("Get Cart Request")
        .get("/carts")
        .header("Authorization", "Bearer ${auth_token}")
        .header("Content-Type", "application/json")
        .check(status.is(200))
        .check(jsonPath("$.data[0].id").saveAs("cart_uuid"))

    val addToCartRequest = http("Add offer to Cart Request")
        .post("/carts/${cart_uuid}/items")
        .body(StringBody("""{"data": {"type": "items", "attributes": {"sku": "${product_sku}", "productOfferReference": "${product_offer_reference}", "quantity": 10}}}""")).asJson
        .header("Authorization", "Bearer ${auth_token}")
        .header("Content-Type", "application/json")
        .check(status.is(201))

    val checkoutRequest = http(scenarioName)
        .post("/checkout")
        .body(StringBody("""{"data": {"type": "checkout", "attributes": {"customer": {"salutation": "Mr", "email": "${email}", "firstName": "Spryker", "lastName": "Customer"}, "idCart": "${cart_uuid}", "billingAddress": {"salutation": "Mr", "email": "${email}", "firstName": "Spryker", "lastName": "Customer", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "${email}", "firstName": "Spryker", "lastName": "Customer", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
        .header("Authorization", "Bearer ${auth_token}")
        .header("Content-Type", "application/json")
        .check(status.is(201))

    val scn = scenario(scenarioName)
        .feed(customerFeeder)
        .repeat(1) {
            feed(productFeeder)
                .exec(getAccessTokenRequest)
                .exec(getCartRequest)
                .exec(addToCartRequest)
                .exec(checkoutRequest)
        }
}

class CheckoutWithOffersApiRamp extends Simulation with CheckoutWithOffersApiBase {

    override lazy val scenarioName = "Add Offer to Cart Api [Incremental]"

    setUp(scn.inject(
        rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
        .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
        .protocols(httpProtocol)
}

class CheckoutWithOffersApiSteady extends Simulation with CheckoutWithOffersApiBase {

    override lazy val scenarioName = "Add Offer to Cart Api [Steady RPS]"

    setUp(scn.inject(
        constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
        .throttle(
            jumpToRps(Scenario.targetRps),
            holdFor(Scenario.duration),
        )
        .protocols(httpProtocol)
}
