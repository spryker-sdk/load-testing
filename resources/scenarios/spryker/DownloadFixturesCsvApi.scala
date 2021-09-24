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

trait DownloadFixturesCsvApiBase {

  lazy val scenarioName = "Download Fixtures Csv Api"

  val httpProtocol = GlueProtocol.httpProtocol

  val downloadFixturesCsvRequest = http("Download Fixtures Csv Api")
    .post("/customer-fixture")
    .body(StringBody("""{"data":{"type":"customer-fixture","attributes":{}}}""")).asJson
    .check(
      jsonPath("$.data.attributes.accessToken").saveAs("accessToken")
    ).check(
      jsonPath("$.data.attributes.cartId").saveAs("cartId")
    )

  val scn = scenario(scenarioName)
      .exec(downloadFixturesCsvRequest)
      .pause(20, 25)
      .exec(session => {
        val accessToken = session("accessToken").as[String].trim
        val cartId = session("cartId").as[String].trim
        println(Scenario.targetRps.toDouble);
        println(accessToken)
        println(cartId)

  val filePath = "tests/_data/customer_fixtures.csv";
   val s1 = new File(filePath)
    if(s1.exists()){
      val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
      writer.write(accessToken)
      writer.write(",")
      writer.write(cartId)
      writer.write("\n")
      writer.close()
    }
    else {
      val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
      writer.println("access_token,cart_id")
      writer.write(accessToken)
      writer.write(",")
      writer.write(cartId)
      writer.write("\n")
      writer.close()
    }
      session
      })
}

class DownloadFixturesCsvApiRamp extends Simulation with DownloadFixturesCsvApiBase {

  override lazy val scenarioName = "Download Fixtures Csv Api [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(0) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration))
    .protocols(httpProtocol)

}

class DownloadFixturesCsvApiSteady extends Simulation with DownloadFixturesCsvApiBase {

  override lazy val scenarioName = "Download Fixtures Csv Api [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(0.1) during (Scenario.duration),
    ))
    .protocols(httpProtocol)
}
