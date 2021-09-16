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
    .post("/carts/13f5aabc-48d3-5da6-a591-cce470de39a1/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"041498248932","quantity":1,"idPromotionalItem":"string","productOfferReference":"041498248932_474-001","merchantReference":"474-001","salesUnit":{"id":0,"amount":2},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJmcm9udGVuZCIsImp0aSI6IjdlN2QyNGIwOGIyZTljMjk4NzliMzFiNmZjMDY2MTkyYjc3ZGM4MDY3OWNmZmY5ZDRkNjhhZDA3ZTkyYjM3Y2M2YWQ2MDIwNjg5OGFmODU0IiwiaWF0IjoxNjMxNjQyNDkwLjM0NTIwODksIm5iZiI6MTYzMTY0MjQ5MC4zNDUyMTUxLCJleHAiOjE2MzE2NzEyOTAuMjM4NTQ3MSwic3ViIjoie1wiaWRfYWdlbnRcIjpudWxsLFwiY3VzdG9tZXJfcmVmZXJlbmNlXCI6XCJVUy1RQS0yMlwiLFwiaWRfY3VzdG9tZXJcIjoyMn0iLCJzY29wZXMiOlsiY3VzdG9tZXIiXX0.Y4QfLcSXZUBgFUjNMzOvgThmEW0Q602kBxtVGH_KLbavRn9rpK5x2eb-sj4qEn7SqwHGq0IZOrQXtHaaR5MOAZD7Z5cfJvkp2neduDNSJ-KckY0sZJOAbiIwx4W0jP003kV2YPEIqP4ZFJKzYAt9CV4xgCkkSrNMwMuOT1hbGpATrFRy281o_7UrOvBvKxvcZg2HCVQkMV6HG7kee4cPcR-wJeZ3Pzo6PiLsq6ZAGMHuVn2YYngAzppme6-xZbqAWlgSqy7sZ_gdfEeNFgFMFIyzlphGkTu0GmJmtGr7p1dLzmx7CLrvKYmQasttiY-hdW_nfGPxy_9CiVf0E0uaAg")
    .check(status.is(201))

  val scn = scenario(scenarioName)
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
