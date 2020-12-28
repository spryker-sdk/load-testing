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
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import java.lang.System.getProperty
import scala.concurrent.duration.{Duration, MILLISECONDS}
import scala.util.Random

trait GuestCheckoutCustomerApiBase {
  lazy val scenarioName = "Guest Checkout Customer Api"

  val httpProtocol: HttpProtocolBuilder = GlueProtocol.httpProtocol

  val PRODUCT_COUNT_FOR_CART: Int = getProperty("PRODUCT_COUNT", "70").toInt

  val VISIT_PAGE_LIST_SESSION_VARIABLE_NAME = "visitPageList"
  val PRODUCT_ID_LIST_SESSION_VARIABLE_NAME = "productIdList"
  val MAX_PAGES_SESSION_VARIABLE_NAME = "maxPages"
  val MAX_AVAILABLE_PRODUCT_SESSION_VARIABLE_NAME = "maxAvailableProduct"
  val NEXT_PAGES_SESSION_VARIABLE_NAME = "nextPage"
  val PRODUCT_CONCRETE_ID_LIST_FOR_CURRENT_REQUEST_SESSION_VARIABLE_NAME = "productConcreteIdList"

  val uniqueIdFeeder: Iterator[Map[String, String]] = Iterator.continually(Map("uniqueId" -> (Random.alphanumeric.take(25).mkString)))

  val getRandomProductRequest: HttpRequestBuilder = http("Catalog Search with random page")
    .get("/catalog-search")
    .queryParam("include", "abstract-products")
    .queryParam("page", session => session(NEXT_PAGES_SESSION_VARIABLE_NAME).as[String])
    .queryParam("ipp", "36")
    .check(status.is(200))
    .check(jsonPath("$.data[0].attributes.pagination.maxPage").saveAs(MAX_PAGES_SESSION_VARIABLE_NAME))
    .check(jsonPath("$.data[0].attributes.pagination.numFound").saveAs(MAX_AVAILABLE_PRODUCT_SESSION_VARIABLE_NAME))
    .check(
      jsonPath("$.included[*].attributes.attributeMap.product_concrete_ids[0]")
        .findAll
        .saveAs(PRODUCT_CONCRETE_ID_LIST_FOR_CURRENT_REQUEST_SESSION_VARIABLE_NAME)
    )

  val prepareSessionVariables: ChainBuilder = exec { session =>
    session
      .set(VISIT_PAGE_LIST_SESSION_VARIABLE_NAME, List[Int]())
      .set(PRODUCT_ID_LIST_SESSION_VARIABLE_NAME, List[Int]())
      .set(NEXT_PAGES_SESSION_VARIABLE_NAME, "1")
  }

  val updateVisitedPagesSessionVariable: ChainBuilder = exec { session: Session =>
    val maxPages: Int = session(MAX_PAGES_SESSION_VARIABLE_NAME).as[Int]
    var visitPageList = session(VISIT_PAGE_LIST_SESSION_VARIABLE_NAME).as[List[Int]]

    var randomPage: Int = Random.between(1, maxPages + 1)

    while (visitPageList.contains(randomPage)) {
      randomPage = Random.between(1, maxPages + 1)
    }

    visitPageList ::= randomPage
    session
      .set(VISIT_PAGE_LIST_SESSION_VARIABLE_NAME, visitPageList)
      .set(NEXT_PAGES_SESSION_VARIABLE_NAME, randomPage)
  }

  val updateProductListSessionVariable: ChainBuilder = exec { session: Session =>
    var productIdList = session(PRODUCT_ID_LIST_SESSION_VARIABLE_NAME).as[List[String]]

    if (session("statusCode").as[Int] == 201) {
      productIdList ::= session("productId").as[String]
    }

    session.set(PRODUCT_ID_LIST_SESSION_VARIABLE_NAME, productIdList)
  }

  def getGuestCartUrl(session: Session): String = {
    val guestCartUrl = "/guest-cart-items"

    if (!session.contains("cart_uuid")) {
      return guestCartUrl
    }

    "/guest-carts/" + session("cart_uuid").as[String] + guestCartUrl
  }

  val addToCartRequest: HttpRequestBuilder = http("Add each product to Guest Cart")
    .post(session => getGuestCartUrl(session))
    .body(StringBody("""{"data": {"type": "guest-cart-items", "attributes": {"sku": "${productId}", "quantity": 3}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${uniqueId}")
    .header("Content-Type", "application/json")
    .check(status.saveAs("statusCode"))
    .check(status.is(201))
    .check(jsonPath("$.data.id").saveAs("cart_uuid"))

  val checkoutDataRequest: HttpRequestBuilder = http("Checkout data Guest cart")
    .post("/checkout-data")
    .body(StringBody("""{"data": {"type": "checkout-data", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "idCart": "${cart_uuid}", "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${uniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(200))

  val checkoutRequest: HttpRequestBuilder = http("Checkout Guest cart")
    .post("/checkout")
    .body(StringBody("""{"data": {"type": "checkout", "attributes": {"customer": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest"}, "idCart": "${cart_uuid}", "billingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "shippingAddress": {"salutation": "Mr", "email": "spryker.guest@test.com", "firstName": "Spryker", "lastName": "Guest", "address1": "West road", "address2": "212", "address3": "", "zipCode": "61000", "city": "Berlin", "iso2Code": "DE", "company": "Spryker", "phone": "+380669455897", "isDefaultShipping": true, "isDefaultBilling": true}, "payments": [{"paymentMethodName": "Invoice", "paymentProviderName": "DummyPayment"}], "shipment": {"idShipmentMethod": 1}}}}""")).asJson
    .header("X-Anonymous-Customer-Unique-Id", "${uniqueId}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val scn: ScenarioBuilder = scenario(scenarioName)
    .feed(uniqueIdFeeder)
    .exec(prepareSessionVariables)
    .exec(getRandomProductRequest)
    .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
    .doWhile(session => session(PRODUCT_ID_LIST_SESSION_VARIABLE_NAME).as[List[String]].length < PRODUCT_COUNT_FOR_CART) {
      exec(getRandomProductRequest)
        .exec(updateVisitedPagesSessionVariable)
        .foreach(session => session(PRODUCT_CONCRETE_ID_LIST_FOR_CURRENT_REQUEST_SESSION_VARIABLE_NAME).as[Vector[String]], "productId") {
          doIfEquals(session => session(PRODUCT_ID_LIST_SESSION_VARIABLE_NAME).as[List[String]].length < PRODUCT_COUNT_FOR_CART, true) {
            exec(addToCartRequest)
              .exec(updateProductListSessionVariable)
              .pause(Duration(500, MILLISECONDS), Duration(3000, MILLISECONDS))
          }
        }
    }
    .exec(checkoutDataRequest)
    .exec(checkoutRequest)
}

class GuestCheckoutCustomerApiRamp extends Simulation with GuestCheckoutCustomerApiBase {
  override lazy val scenarioName = "Guest Checkout Customer API [Incremental]"

  setUp(scn.inject(
    rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}

class GuestCheckoutCustomerApiSteady extends Simulation with GuestCheckoutCustomerApiBase {
  override lazy val scenarioName = "Guest Checkout Customer API [Steady RPS]"

  setUp(scn.inject(
    constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration)
  )).protocols(httpProtocol)
}
