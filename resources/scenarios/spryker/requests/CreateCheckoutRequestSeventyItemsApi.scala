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
import spryker.YvesProtocol._
import spryker.FeProtocol._
import spryker.Scenario._
import io.gatling.core.feeder._
import spryker.Encryptor._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import spryker.CreateCustomerRequestApi._
import spryker.CreateAccessTokenRequestApi._
import spryker.CreateCartRequestApi._
import spryker.AddToCartRequestApi._

object CreateCheckoutRequestSeventyItemsApi {

  lazy val scenarioName = "Checkout with 70 items Full Flow Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val txndatetime = DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm:ss").format(LocalDateTime.now)
  val approvalCode = "Y:OK1989:4561642903:YYYX:908035"

  val previewPickupTimeslotsRequest = http("Retrieves list of merchant pickup timeslots for preview.")
    .get("/preview-pickup-timeslots?merchantReference=${merchant_reference}")
    .check(jsonPath("$.data[1].id").saveAs("merchant_timeslot_id"))
    .check(status.is(200))

  val productConcreteFeeder = csv("tests/_data/product_concrete.csv").random

  val feeder = Iterator.continually(Map(
    "customer_email" -> (Random.alphanumeric.take(30).mkString + "@gmail.com"), 
    "password" -> "supe!rsEcu1re", 
    "payment_token" -> (Random.alphanumeric.take(8).mkString.toUpperCase() + "-" + Random.alphanumeric.take(4).mkString.toUpperCase() + "-" + Random.alphanumeric.take(4).mkString.toUpperCase() + "-" + Random.alphanumeric.take(4).mkString.toUpperCase() + "-" + Random.alphanumeric.take(12).mkString.toUpperCase())
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

  val addToCartRequest = http("Add to Cart Request")
    .post("/carts/${cart_id}/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"${sku}","quantity":2,"idPromotionalItem":"string","productOfferReference":"${product_offer_reference}","merchantReference":"${merchant_reference}","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))
    .check(header("ETag").saveAs("if_match_header"))

  val updateCartRequest = http("Update Cart Request")
    .patch("/carts/${cart_id}")
    .body(StringBody("""{"data":{"type":"carts","attributes":{"priceMode":"NET_MODE","currency":"USD","store":"US","merchantReference":"${merchant_reference}","merchantSelections":{"serviceType":"pickup","merchantFilterAddress":{"address1":"Kharkiv Ukraine","zipCode":"12345"}},"merchantTimeslot":{"merchantTimeslotId":"${merchant_timeslot_id}","merchantTimeslotReservation":{"merchantTimeslotReservationId":1,"expirationDate":"2022:03:01-14:37:45"}},"tipAmount":20}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .header("If-Match", "${if_match_header}")
    .check(status.is(200))

  val merchantTimeslotReservationsRequest = http("Reserve pre-selected timeslot from a cart.")
    .post("/carts/${cart_id}/merchant-timeslot-reservations")
    .body(StringBody("""{"data":{"type":"merchant-timeslot-reservations","attributes":{}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("If-Match", "${if_match_header}")
    .check(status.is(201))

  val checkoutDataRequest = http("Checkout First Data Request")
    .post("/checkout-data")
    .body(StringBody("""{"data":{"type":"checkout-data","attributes":{"customer":{"salutation":"Mr","email":"${customer_email}","firstName":"name","lastName":"name"},"idCart":"${cart_id}","cartNote":"this is cart note","billingAddress":{"salutation":"Mr","firstName":"John","lastName":"Doe","address1":"billing","address2":"b","address3":"aaa","zipCode":"12312","city":"Huston","country":"USA","iso2Code":"US","phone":"1234567890","regionIso2Code":"US-IL"},"payments":[{"paymentMethodName":"firstDataCreditCard","paymentProviderName":"firstData"}],"shipment":{"idShipmentMethod":2}}}}"""))
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(200))
    .check(
      jsonPath("$.data.attributes.firstDataCreditCardParameters.oid").saveAs("oid"),
      jsonPath("$.data.attributes.firstDataCreditCardParameters.chargetotal").saveAs("chargetotal"),
      jsonPath("$.data.attributes.firstDataCreditCardParameters.currency").saveAs("currency"),
      jsonPath("$.data.attributes.firstDataCreditCardParameters.storename").saveAs("storename")
    )

  val serverToServerNotificationRequest = http("Server to Server Notification Request")
        .post(YvesProtocol.baseUrl + "/first-data-notifications")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .formParam("timezone", "Etc/UTC")
        .formParam("txndatetime", DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm:ss").format(LocalDateTime.now))
        .formParam("txndate_processed", "21/04/21 15:49:08")
        .formParam("hash_algorithm", "HMACSHA256")
        .formParam("oid", session => session("oid").as[String])
        .formParam("ipgTransactionId", "84561642903")
        .formParam("txntype", "preauth")
        .formParam("approval_code", "Y:OK1989:4561642903:YYYX:908035")
        .formParam("status", "APPROVED")
        .formParam("paymentMethod", "V")
        .formParam("currency", session => session("currency").as[String])
        .formParam("chargetotal", session => session("chargetotal").as[String])
        .formParam("storename", session => session("storename").as[String])
        .formParam("transactionNotificationURL", "https://test.ipg-online.com/webshop/transactionNotification")
        .formParam("notification_hash", session => Encryptor.getNotificationHash(      
              session("chargetotal").as[String].trim,
              session("currency").as[String].trim,
              txndatetime,
              session("storename").as[String].trim,
              approvalCode
          ))
        .formParam("terminal_id", "1609839")
        .formParam("endpointTransactionId", "908035")
        .formParam("processor_response_code", "00")
        .formParam("hosteddataid", session => session("payment_token").as[String])
        .formParam("associationResponseCode", "000")
        .formParam("processor_network_information", "VISA")
        .formParam("schemeTransactionId", "011111818998118")
        .formParam("installments_interest", "false")
        .formParam("tdate", "1619012948")
        .formParam("sname", "Max Mustermann")
        .formParam("saddr1", "Solarstrasse 7")
        .formParam("saddr2", "saddr22")
        .formParam("szip", "4653")
        .formParam("scity", "Eberstalzell")
        .formParam("sstate", "NY")
        .formParam("scountry", "AT")
        .formParam("bname", "Max Mustermann")
        .formParam("baddr1", "Solarstrasse 7")
        .formParam("baddr2", "baddress2")
        .formParam("bzip", "4653")
        .formParam("bcity", "Eberstalzell")
        .formParam("bstate", "NY")
        .formParam("bcountry", "AT")
        .formParam("customerid", "customerID-1")
        .formParam("phone", "1234567890")
        .formParam("email", session => session("customer_email").as[String])
        .formParam("refnumber", "84561642903 ")
        .formParam("cardnumber", "value=\"(VISA) ... 7777\"")
        .formParam("ccbrand", "VISA")
        .formParam("ccbin", "401200")
        .formParam("expyear", "2024")
        .formParam("expmonth", "12")
        .formParam("cccountry", "N/A")
        .formParam("customParam_SuccessFeUrl", FeProtocol.successFeUrl)
        .formParam("customParam_FailureFeUrl", FeProtocol.failureFeUrl)
        .formParam("customParam_PropertyHash", session => Encryptor.getCustomPropHash(
              session("oid").as[String].trim,
              session("storename").as[String].trim,
              session("currency").as[String].trim,
              session("chargetotal").as[String].trim,
              FeProtocol.successFeUrl,
              FeProtocol.failureFeUrl
          ))
        .check(status.is(200))

  val checkoutRequest = http("Checkout Full Flow Api")
    .post("/checkout")
    .body(StringBody("""{"data":{"type":"checkout","attributes":{"customer":{"salutation":"Mr","email":"sonia@spryker.com","firstName":"name","lastName":"name","phone":"1234567890","dateOfBirth": "1980-10-23"},"idCart":"${cart_id}","cartNote":"this is cart note","billingAddress":{"salutation":"Mr","firstName":"John","lastName":"Doe","address1":"billing","address2":"b","address3":"aaa","zipCode":"32836","city":"Huston","country":"USA","iso2Code":"US","phone":"1234567890","regionIso2Code":"US-IL"},"shipment":{"idShipmentMethod":2},"payments":[{"paymentMethodName":"firstDataCreditCard","paymentProviderName":"firstData","savePaymentMethod":true}],"shippingAddress":{"salutation":"Mr","firstName":"John","lastName":"Doe","address1":"not used","address2":"b","address3":"aaa","zipCode":"32836","city":"Chicago","country":"USA","iso2Code":"US","phone":"1234567890"}}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.saveAs("checkout_status"))
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.orderReference").saveAs("order_reference"))
    .check(bodyString.saveAs("checkoutRequestResponse"))

  val executeRequest = scenario(scenarioName)
    .feed(feeder)
    .feed(productConcreteFeeder)
    .exec(createCustomerRequest)
    .exec(accessTokenRequest)
    .exec(previewPickupTimeslotsRequest)
    .exec(createCartRequest)
    .repeat(70) {
      exec(addToCartRequest)
    }
    .exec(updateCartRequest)
    .exec(merchantTimeslotReservationsRequest)
    .exec(checkoutDataRequest)
    .exec(serverToServerNotificationRequest)
    .pause(3)
    .exec(checkoutRequest)
    .doIfEquals("${checkout_status}", "422") {
      exec(serverToServerNotificationRequest)
      .exec(checkoutRequest)
    }
}