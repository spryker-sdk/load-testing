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
import spryker.YvesProtocol._
import spryker.Scenario._

trait PlaceOrderCustomerBase {
  lazy val scenarioName = "Place Order as Customer"

  val httpProtocol = YvesProtocol.httpProtocol
  val productFeeder = csv("tests/_data/product_concrete.csv").random
  val customerFeeder = csv("tests/_data/customer.csv").random

  val pdpRequest = http("PDP Request")
    .get("${pdp_url}")
    .check(status.is(200))
    .check(css("input[name=\"add_to_cart_form[_token]\"]", "value").saveAs("addToCartFormCsrfToken"))

  val addToCartRequest = http("Add to Cart request")
    .post("/en/cart/add/${sku}")
    .formParam("quantity", "10")
    .formParam("add_to_cart_form[_token]", "${addToCartFormCsrfToken}")

  val guestLoginPageRequest = http("Guest Login Page Request")
    .get("/en/checkout/customer")
    .check(status.is(200))
    .check(css("input[name=\"guestForm[_token]\"]", "value").saveAs("loginFormCsrfToken"))

  val guestLoginRequest = http("Guest Login Request")
    .post("/en/checkout/customer")
    .formParam("guestForm[customer][email]", "${email}")
    .formParam("guestForm[customer][salutation]", "Mr")
    .formParam("guestForm[customer][first_name]", "test")
    .formParam("guestForm[customer][last_name]", "test")
    .formParam("guestForm[customer][is_guest]", 1)
    .formParam("guestForm[customer][accept_terms]", 1)
    .formParam("guestForm[_token]", "${loginFormCsrfToken}")

  val loginPageRequest = http("Login Page Request")
    .get("/en/login")
    .check(status.is(200))
    .check(css("input[name=\"loginForm[_token]\"]", "value").saveAs("loginFormCsrfToken"))

  val loginRequest = http("Login Request")
    .post("/login_check")
    .formParam("loginForm[email]", "${email}")
    .formParam("loginForm[password]", "${password}")
    .formParam("loginForm[remember_me]", 1)
    .formParam("loginForm[_token]", "${loginFormCsrfToken}")

  val checkoutRequest = http("Checkout Address Page Request")
    .get("/en/checkout/address")
    .check(css("input[name=\"addressesForm[_token]\"]", "value").saveAs("checkoutAddressCsrfToken"))

  val checkoutAddressRequest = http("Checkout Address Request")
    .post("/en/checkout/address")
    .formParam("addressesForm[shippingAddress][id_customer_address]", "0")
    .formParam("addressesForm[shippingAddress][salutation]", "Mr")
    .formParam("addressesForm[shippingAddress][first_name]", "test")
    .formParam("addressesForm[shippingAddress][last_name]", "test")
    .formParam("addressesForm[shippingAddress][company]", "test")
    .formParam("addressesForm[shippingAddress][address1]", "test")
    .formParam("addressesForm[shippingAddress][address2]", "123")
    .formParam("addressesForm[shippingAddress][address3]", "")
    .formParam("addressesForm[shippingAddress][zip_code]", "123123")
    .formParam("addressesForm[shippingAddress][city]", "test")
    .formParam("addressesForm[shippingAddress][iso2_code]", "DE")
    .formParam("addressesForm[shippingAddress][phone]", "")
    .formParam("addressesForm[shippingAddress][id_company_unit_address]", "")
    .formParam("addressesForm[shippingAddress][isAddressSavingSkipped]", "1")
    .formParam("addressesForm[billingSameAsShipping]", "1")
    .formParam("addressesForm[billingAddress][id_customer_address]", "0")
    .formParam("addressesForm[billingAddress][salutation]", "Mr")
    .formParam("addressesForm[billingAddress][first_name]", "")
    .formParam("addressesForm[billingAddress][last_name]", "")
    .formParam("addressesForm[billingAddress][company]", "")
    .formParam("addressesForm[billingAddress][address1]", "")
    .formParam("addressesForm[billingAddress][address2]", "")
    .formParam("addressesForm[billingAddress][address3]", "")
    .formParam("addressesForm[billingAddress][zip_code]", "")
    .formParam("addressesForm[billingAddress][city]", "")
    .formParam("addressesForm[billingAddress][iso2_code]", "DE")
    .formParam("addressesForm[billingAddress][phone]", "")
    .formParam("addressesForm[billingAddress][id_company_unit_address]", "")
    .formParam("addressesForm[billingAddress][isAddressSavingSkipped]", "1")
    .formParam("addressesForm[isMultipleShipmentEnabled]", "")
    .formParam("addressesForm[_token]", "${checkoutAddressCsrfToken}")
    .check(css("input[name=\"shipmentCollectionForm[_token]\"]", "value").saveAs("checkoutShipmentCsrfToken"))

  val checkoutShipmentRequest = http("Checkout Shipment Request")
    .post("/en/checkout/shipment")
    .formParam("shipmentCollectionForm[shipmentGroups][0][shipment][shipmentSelection]", "4")
    .formParam("shipmentCollectionForm[_token]", "${checkoutShipmentCsrfToken}")
    .check(css("input[name=\"paymentForm[dummyPaymentInvoice][date_of_birth]\"]").count.saveAs("dummyPaymentInvoiceDateOfBirth"))
    .check(css("input[name=\"paymentForm[_token]\"]", "value").saveAs("checkoutPaymentCsrfToken"))

  def generatePaymentForm(session: Session): Map[String, String] = {
    if (session("dummyPaymentInvoiceDateOfBirth").as[Int] > 0) {
      return Map(
        "paymentForm[paymentSelection]" -> "dummyPaymentInvoice",
        "paymentForm[_token]" -> session("checkoutPaymentCsrfToken").as[String],
        "paymentForm[dummyPaymentInvoice][date_of_birth]" -> "05.05.1995"
      )
    } else {
      return Map(
        "paymentForm[paymentSelection]" -> "dummyPaymentInvoice",
        "paymentForm[_token]" -> session("checkoutPaymentCsrfToken").as[String]
      )
    }
  }

  val checkoutPaymentRequest = http("Checkout Payment Request")
    .post("/en/checkout/payment")
    .formParamMap(session => generatePaymentForm(session))
    .check(css("input[name=\"summaryForm[_token]\"]", "value").saveAs("checkoutSummaryCsrfToken"))

  val placeOrderRequest = http("Place Order Request")
    .post("/en/checkout/summary")
    .formParam("summaryForm[acceptTermsAndConditions]", "1")
    .formParam("summaryForm[_token]", "${checkoutSummaryCsrfToken}")

  val scn = scenario(scenarioName)
    .feed(productFeeder)
    .feed(customerFeeder)
    .repeat(1) {
      exec(loginPageRequest)
        .exec(loginRequest)
        .exec(pdpRequest)
        .exec(addToCartRequest)
        .exec(checkoutRequest)
        .exec(checkoutAddressRequest)
        .exec(checkoutShipmentRequest)
        .exec(checkoutPaymentRequest)
        .exec(placeOrderRequest)
    }
}

class PlaceOrderCustomerRamp extends Simulation with PlaceOrderCustomerBase {

  override lazy val scenarioName = "Place Order as Customer [Incremental]"

  setUp(scn.inject(rampUsers(Scenario.targetRps.toInt) during (Scenario.duration))).maxDuration(Scenario.duration).protocols(httpProtocol)
}

class PlaceOrderCustomerSteady extends Simulation with PlaceOrderCustomerBase {

  override lazy val scenarioName = "Place Order as Customer [Steady RPS]"

  setUp(scn.inject(
    atOnceUsers(Scenario.targetRps.toInt),
  )).maxDuration(Scenario.duration).protocols(httpProtocol)
}
