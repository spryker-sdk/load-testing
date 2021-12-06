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
import scala.tools.nsc.io
import java.io.{File, PrintWriter, FileOutputStream}

trait CsvFixturesAddToCartLessOrEqualFiftyItemsApiBase {

  lazy val scenarioName = "Csv Fixtures Add To Cart Api <= 50 Items"

  val httpProtocol = GlueProtocol.httpProtocol

  val csvFixturesAddToCartRequest = http("Csv Fixtures Add To Cart Api <= 50 Items")
    .post("/customer-fixture")
    .body(StringBody("""{"data":{"type":"customer-fixture","attributes":{"itemsQuantity": 49}}}""")).asJson
    .check(
      jsonPath("$.data.attributes.accessToken").saveAs("accessToken")
    ).check(
      jsonPath("$.data.attributes.cartId").saveAs("cartId")
    )

  val scn = scenario(scenarioName)
      .exec(csvFixturesAddToCartRequest)
      .exec(session => {
        val accessToken = session("accessToken").as[String].trim
        val cartId = session("cartId").as[String].trim

        val filePath = "tests/_data/customer_fixtures_less_or_equal_fifty_items.csv";
        val s1 = new File(filePath)

        val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
        writer.write(accessToken)
        writer.write(",")
        writer.write(cartId)
        writer.write("\n")
        writer.close()
      session
      })
}

class CsvFixturesAddToCartLessOrEqualFiftyItemsApiRamp extends Simulation with CsvFixturesAddToCartLessOrEqualFiftyItemsApiBase {

  override lazy val scenarioName = "Csv Fixtures Add To Cart Api <= 50 Items [Incremental]"

  before {
      val filePath = "tests/_data/customer_fixtures_less_or_equal_fifty_items.csv";
      val pw = new PrintWriter(filePath);
      pw.close();

      val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
      writer.println("access_token,cart_id")
      writer.close()
  }

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)

}

class CsvFixturesAddToCartLessOrEqualFiftyItemsApiSteady extends Simulation with CsvFixturesAddToCartLessOrEqualFiftyItemsApiBase {

  override lazy val scenarioName = "Csv Fixtures Add To Cart Api <= 50 Items [Steady RPS]"

  before {
      val filePath = "tests/_data/customer_fixtures_less_or_equal_fifty_items.csv";
      val pw = new PrintWriter(filePath);
      pw.close();

      val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
      writer.println("access_token,cart_id")
      writer.close()
  }

  setUp(scn.inject(
      constantUsersPerSec(0.1) during (Scenario.duration),
    ))
    .protocols(httpProtocol)
}
