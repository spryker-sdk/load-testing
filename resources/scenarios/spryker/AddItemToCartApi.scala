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
import io.netty.handler.codec.http._
import io.netty.util.CharsetUtil
import java.nio.charset.Charset
import io.gatling.core.akka._
import scala.concurrent.duration._
import scala.util.Random
import spryker.GlueProtocol._

trait AddItemToCartApiBase {

  lazy val scenarioName = "Add Item To Cart Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val feeder = csv("tests/_data/customer_fixtures.csv").random

  val addToCartRequest = http("Add Item To Cart Api")
    .post("/carts/${cart_id}/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":2},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(201))

  val scn = scenario(scenarioName)
  .feed(feeder)
  .exec(addToCartRequest)
}

class AddItemToCartApiRamp extends Simulation with AddItemToCartApiBase {

  override lazy val scenarioName = "Add Item To Cart Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)
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
