package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait CreateAccessTokensFrontendApiBase {

  lazy val scenarioName = "Creates access token for user."

  val httpProtocol = GlueProtocol.httpProtocol

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
  }

class CreateAccessTokensFrontendApiRamp extends Simulation with CreateAccessTokensFrontendApiBase {

  override lazy val scenarioName = "Creates access token for user. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateAccessTokensFrontendApiSteady extends Simulation with CreateAccessTokensFrontendApiBase {

  override lazy val scenarioName = "Creates access token for user. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}