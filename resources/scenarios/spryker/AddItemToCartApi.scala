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

trait AddItemToCartApiBase {

  lazy val scenarioName = "Add Item To Cart Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val addToCartRequest = http("Add Item To Cart Api")
    .post("/carts/a72bbc39-18a5-53a2-a1db-8572f59a817f/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":2},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJmcm9udGVuZCIsImp0aSI6ImI1YzdmYjJhMTM3N2I3NDk4MTJkYTExYWIzYjlmNjAxNDY1NzBmOWMwYzIwM2MxNmY2MzZhYThmM2VkM2ZiMjYzMWVkODljNmQyMTQzODhiIiwiaWF0IjoxNjMxODcyNTY2LjE1MzIzLCJuYmYiOjE2MzE4NzI1NjYuMTUzMjQ1OSwiZXhwIjoxNjMxOTAxMzY2LjA4ODg1NSwic3ViIjoie1wiaWRfYWdlbnRcIjpudWxsLFwiY3VzdG9tZXJfcmVmZXJlbmNlXCI6XCJVUy0tMjJcIixcImlkX2N1c3RvbWVyXCI6MjJ9Iiwic2NvcGVzIjpbImN1c3RvbWVyIl19.K3lSzqHmBdobATXyz_DjUXfnf233R2rNEZ1pbD3TtH9dzO3yx0BbWgfvjLw0AzNXIaBL88Jlq867OevTcL0PwP-6yN3toQ_VROtjkGAExLu-E4ixaTfENbTSPcnsPZtp-opnhrenXNMte0XgzIw7gam3Ke8hDSOcU_2r1Jtey7I5oHMPsfLeBVqutm1d1iKlaWHEQtEGvIVmqqg0h8__uYEI5-v2vo5yCpEr4u-EMv0YzNcg2fOVyI_RpN9Ngo0MhL-tG0HEugOXtfJ7OzLAdWXi6WN0KPcjbU9a8arIsy34gBeLSOnAF8RfpdO8pxT8-UiFRAQk4-vXmubl-NxNng")
    .check(status.is(201))

  val scn = scenario(scenarioName)
      .exec(addToCartRequest)


}

class AddItemToCartApiRamp extends Simulation with AddItemToCartApiBase {

  override lazy val scenarioName = "Add Item To Cart Api [Incremental]"

        val createUserRequest = http("Add Customer")
        .post("/customers")
        .body(StringBody("""{"data":{"type":"customers","attributes":{"firstName":"Paul","lastName":"Rosenberg","gender":"Male","salutation":"Mr","email":"zingeon4@gmail.com","password":"supe!rsEcu1re","confirmPassword":"supe!rsEcu1re","acceptedTerms":true}}}""")).asJson
        .check(status.is(201))

        val createUserScenario = scenario(scenarioName)
        .exec(createUserRequest).inject(
              rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
            )

  val targetUrl = loadTarget()
  println("Simulation!")
  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)

      def loadTarget() = {

    }
}

class AddItemToCartApiSteady extends Simulation with AddItemToCartApiBase {

  override lazy val scenarioName = "Add Item To Cart Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}
