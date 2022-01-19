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

object CreateCheckoutRequestApi {

  val txndatetime = DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm:ss").format(LocalDateTime.now)
  val approvalCode = "Y:OK1989:4561642903:YYYX:908035"

  val previewPickupTimeslotsRequest = http("Retrieves list of merchant pickup timeslots for preview.")
    .get("/preview-pickup-timeslots?merchantReference=${merchant_reference}")
    .check(jsonPath("$.data[0].id").saveAs("merchant_timeslot_id"))
    .check(status.is(200))

  val productConcreteFeeder = csv("tests/_data/product_concrete.csv").random

  val updateCartRequest = http("Update Cart Request")
    .patch("/carts/${cartId}")
    .body(StringBody("""{"data":{"type":"carts","attributes":{"priceMode":"NET_MODE","currency":"USD","store":"US","merchantReference":"${merchant_reference}","merchantSelections":{"serviceType":"pickup","merchantFilterAddress":{"address1":"Kharkiv Ukraine","zipCode":"12345"}},"merchantTimeslot":{"merchantTimeslotId":"${merchant_timeslot_id}","merchantTimeslotReservation":{"merchantTimeslotReservationId":1,"expirationDate":"2022:03:01-14:37:45"}},"tipAmount":20}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .header("If-Match", "${if_match_header}")
    .check(status.is(200))
    .check(bodyString.saveAs("updateCartRequestResponse"))

  val addToCartRequest = http("Add to Cart Request")
    .post("/carts/${cartId}/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"${sku}","quantity":1,"idPromotionalItem":"string","productOfferReference":"${product_offer_reference}","merchantReference":"${merchant_reference}","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val merchantTimeslotReservationsRequest = http("Reserve pre-selected timeslot from a cart.")
    .post("/carts/${cartId}/merchant-timeslot-reservations")
    .body(StringBody("""{"data":{"type":"merchant-timeslot-reservations","attributes":{}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("If-Match", "${if_match_header}")
    .check(status.is(201))

  val checkoutDataRequest = http("Checkout First Data Request")
    .post("/checkout-data")
    .body(StringBody("""{"data":{"type":"checkout-data","attributes":{"customer":{"salutation":"Mr","email":"${customerEmail}","firstName":"name","lastName":"name"},"idCart":"${cartId}","cartNote":"this is cart note","billingAddress":{"salutation":"Mr","firstName":"John","lastName":"Doe","address1":"billing","address2":"b","address3":"aaa","zipCode":"12312","city":"Huston","country":"USA","iso2Code":"US","phone":"1234567890","regionIso2Code":"US-IL"},"payments":[{"paymentMethodName":"firstDataCreditCard","paymentProviderName":"firstData"}],"shipment":{"idShipmentMethod":2}}}}"""))
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
      .formParam("txndatetime", txndatetime)
      .formParam("txndate_processed", "21/04/21 15:49:08")
      .formParam("hash_algorithm", "HMACSHA256")
      .formParam("oid", "${oid}")
      .formParam("ipgTransactionId", "84561642903")
      .formParam("txntype", "preauth")
      .formParam("approval_code", approvalCode)
      .formParam("status", "APPROVED")
      .formParam("paymentMethod", "V")
      .formParam("currency", "${currency}")
      .formParam("chargetotal", "${chargetotal}")
      .formParam("storename", "${storename}")
      .formParam("transactionNotificationURL", "https://test.ipg-online.com/webshop/transactionNotification")
      .formParam("notification_hash", "${notification_hash}")
      .formParam("terminal_id", "1609839")
      .formParam("endpointTransactionId", "908035")
      .formParam("processor_response_code", "00")
      .formParam("hosteddataid", "3E3C8F77-48A4-4EDB-A2E5-A3FB8C697F09")
      .formParam("associationResponseCode", "000")
      .formParam("processor_network_information", "VISA")
      .formParam("schemeTransactionId", "011111818998118")
      .formParam("installments_interest", "false")
      .formParam("tdate", "1619012948")
      .formParam("sname", "John Doe")
      .formParam("saddr1", "saddr 1")
      .formParam("saddr2", "saddr 2")
      .formParam("szip", "4653")
      .formParam("scity", "City 17")
      .formParam("sstate", "NY")
      .formParam("scountry", "AT")
      .formParam("bname", "John Doe")
      .formParam("baddr1", "saddr 1")
      .formParam("baddr2", "saddr 2")
      .formParam("bzip", "4653")
      .formParam("bcity", "City 17")
      .formParam("bstate", "NY")
      .formParam("bcountry", "AT")
      .formParam("customerid", "customerID-1")
      .formParam("phone", "1234567890")
      .formParam("email", "${customerEmail}")
      .formParam("refnumber", "84561642903")
      .formParam("cardnumber", "value=\"(VISA) ... 7777\"")
      .formParam("ccbrand", "VISA")
      .formParam("ccbin", "401200")
      .formParam("expyear", "2024")
      .formParam("expmonth", "12")
      .formParam("cccountry", "N/A")
      .formParam("customParam_SuccessFeUrl", FeProtocol.successFeUrl)
      .formParam("customParam_FailureFeUrl", FeProtocol.failureFeUrl)
      .formParam("customParam_PropertyHash", "${custom_prop_hash}")
      .check(status.is(200))

  val checkoutRequest = http("Checkout Full Flow Api")
    .post("/checkout")
    .body(StringBody("""{"data":{"type":"checkout","attributes":{"customer":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"${customerEmail}","dateOfBirth":"1957-10-23","phone":"1 800-123-0000"},"idCart":"${cartId}","billingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"payments":[{"paymentProviderName":"DummyPayment","paymentMethodName":"Invoice"}],"shipment":{"idShipmentMethod":3},"shipments":[{"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","iso2Code":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regionIso2Code":"US-CA"},"items":["string"],"idShipmentMethod":3,"requestedDeliveryDate":"2022-09-19"}],"cartNote":"string"}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.orderReference").saveAs("order_reference"))
    .check(bodyString.saveAs("checkoutRequestResponse"))

  val executeRequest = feed(productConcreteFeeder)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
    .exec(previewPickupTimeslotsRequest)
    .exec(updateCartRequest)
    .exec(addToCartRequest)
    .exec(merchantTimeslotReservationsRequest)
    .exec(checkoutDataRequest)
    .exec(session => {
      val notificationHash = Encryptor.getNotificationHash(      
              session("chargetotal").as[String].trim,
              session("currency").as[String].trim,
              txndatetime,
              session("storename").as[String].trim,
              approvalCode
          )

      val customPropHash = Encryptor.getCustomPropHash(
              session("oid").as[String].trim,
              session("storename").as[String].trim,
              session("currency").as[String].trim,
              session("chargetotal").as[String].trim,
              FeProtocol.successFeUrl,
              FeProtocol.failureFeUrl
          )

        session.setAll(
          "notification_hash" -> notificationHash.toString,
          "custom_prop_hash" -> customPropHash.toString,
          "oid" -> session("oid").as[String].trim,
          "storename" -> session("storename").as[String].trim,
          "currency" -> session("currency").as[String].trim,
          "chargetotal" -> session("chargetotal").as[String].trim,
          "customerEmail" -> session("customerEmail").as[String].trim,
          "access_token" -> session("access_token").as[String].trim,
          "cartId" -> session("cartId").as[String].trim,
          )
      })
    // .pause(3)
    .exec(serverToServerNotificationRequest)
    // .pause(3)
    .exec(checkoutRequest)
}