package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCustomerRequestApi._
import spryker.CreateAccessTokenRequestApi._
import spryker.CreateCartRequestApi._
import spryker.AddToCartRequestApi._
import scala.util.Random

trait UpdateCustomerPasswordFrontendApiBase {

  lazy val scenarioName = "Updates customer password."

  val httpProtocol = GlueProtocol.httpProtocol

  val newPassword = "supe!rsEcu1re_n3w"

  val request = http(scenarioName)
    .patch("/customer-password/${customerId}")
    .body(StringBody("""{"data":{"type":"customer-password","attributes":{"password":"${password}","newPassword":"""" + newPassword + """","confirmPassword":"""" + newPassword + """"}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(204))

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(request)
  }

class UpdateCustomerPasswordFrontendApiRamp extends Simulation with UpdateCustomerPasswordFrontendApiBase {

  override lazy val scenarioName = "Updates customer password. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class UpdateCustomerPasswordFrontendApiSteady extends Simulation with UpdateCustomerPasswordFrontendApiBase {

  override lazy val scenarioName = "Updates customer password. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}