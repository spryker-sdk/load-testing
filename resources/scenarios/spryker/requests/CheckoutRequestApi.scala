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
import scala.util.Random

object CheckoutRequestApi {
  val request = http("Checkout Request")
    .post("/checkout")
    .body(StringBody("""{"data":{"type":"checkout","attributes":{"customer":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"${customerEmail}","dateOfBirth":"1957-10-23","phone":"1 800-123-0000"},"idCart":"${cartId}","billingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","countryIsoCode":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regioncountryIsoCode":"US-CA"},"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","countryIsoCode":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regioncountryIsoCode":"US-CA"},"payments":[{"paymentProviderName":"DummyPayment","paymentMethodName":"Invoice"}],"shipment":{"idShipmentMethod":3},"shipments":[{"shippingAddress":{"salutation":"Mr","firstName":"Paul","lastName":"Rosenberg","address1":"203 Fifth Ave","address2":"17th floor","address3":"Office 1716","zipCode":"10013","city":"New York","countryIsoCode":"US","company":"Spryker","phone":"1 800-123-0000","isDefaultBilling":true,"isDefaultShipping":true,"regioncountryIsoCode":"US-CA"},"items":["string"],"idShipmentMethod":3,"requestedDeliveryDate":"2022-09-19"}],"cartNote":"string"}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))
    .check(jsonPath("$.data.attributes.orderReference").saveAs("orderId"))

  val executeRequest = exec(request)
}
