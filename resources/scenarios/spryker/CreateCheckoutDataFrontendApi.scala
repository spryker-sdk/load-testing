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

trait CreateCheckoutDataFrontendApiBase {

  lazy val scenarioName = "Checkout Data Full Flow Api"

  val productConcreteFeeder = csv("tests/_data/product_concrete.csv").random

  val checkoutDataRequest = http("Checkout First Data Request")
      .post("/checkout-data")
      .body(StringBody("""{"data":{"type":"checkout-data","attributes":{"customer":{"salutation":"Mr","email":"${customerEmail}","firstName":"name","lastName":"name"},"idCart":"${cartId}","cartNote":"this is cart note","billingAddress":{"salutation":"Mr","firstName":"John","lastName":"Doe","address1":"billing","address2":"b","address3":"aaa","zipCode":"12312","city":"Huston","country":"USA","iso2Code":"US","phone":"1234567890","regionIso2Code":"US-IL"},"payments":[{"paymentMethodName":"firstDataCreditCard","paymentProviderName":"firstData"}],"shipment":{"idShipmentMethod":"2"}}}}"""))
      .header("Authorization", "Bearer ${access_token}")
      .header("Content-Type", "application/json")
      .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
    .exec(AddToCartRequestApi.executeRequest)
    .exec(checkoutDataRequest)
}

class CreateCheckoutDataFrontendApiRamp extends Simulation with CreateCheckoutDataFrontendApiBase {

  override lazy val scenarioName = "Checkout Data Full Flow Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCheckoutDataFrontendApiSteady extends Simulation with CreateCheckoutDataFrontendApiBase {

  override lazy val scenarioName = "Checkout Data Full Flow Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}