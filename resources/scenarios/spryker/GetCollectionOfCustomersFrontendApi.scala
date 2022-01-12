package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait GetCollectionOfCustomersFrontendApiBase {

  lazy val scenarioName = "Retrieves customers collection."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/customers?include=addresses&include=merchants")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCustomersFrontendApiRamp extends Simulation with GetCollectionOfCustomersFrontendApiBase {

  override lazy val scenarioName = "Retrieves customers collection. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCustomersFrontendApiSteady extends Simulation with GetCollectionOfCustomersFrontendApiBase {

  override lazy val scenarioName = "Retrieves customers collection. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}