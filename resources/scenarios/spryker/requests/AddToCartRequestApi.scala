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

object AddToCartRequestApi {
  val feeder = csv("tests/_data/product_concrete.csv").random

  val request = http("Add to Cart Request")
    .post("/carts/${cartId}/items")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"${sku}","quantity":1,"idPromotionalItem":"string","productOfferReference":"${product_offer_reference}","merchantReference":"${merchant_reference}","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .header("Content-Type", "application/json")
    .check(status.is(201))

  val executeRequest = feed(feeder).exec(request)
}
