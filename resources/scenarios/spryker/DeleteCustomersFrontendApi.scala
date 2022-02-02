package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCustomerRequestApi._

trait DeleteCustomersFrontendApiBase {

  lazy val scenarioName = "Anonymizes customers."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .delete("/customers/${customerId}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(204))

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(request)
  }

class DeleteCustomersFrontendApiRamp extends Simulation with DeleteCustomersFrontendApiBase {

  override lazy val scenarioName = "Anonymizes customers. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class DeleteCustomersFrontendApiSteady extends Simulation with DeleteCustomersFrontendApiBase {

  override lazy val scenarioName = "Anonymizes customers. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}