package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait DeleteRefreshTokensFrontendApiBase {

  lazy val scenarioName = "Revokes customer's refresh token."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .delete("/refresh-tokens/${refresh_token}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(204))

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class DeleteRefreshTokensFrontendApiRamp extends Simulation with DeleteRefreshTokensFrontendApiBase {

  override lazy val scenarioName = "Revokes customer's refresh token. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class DeleteRefreshTokensFrontendApiSteady extends Simulation with DeleteRefreshTokensFrontendApiBase {

  override lazy val scenarioName = "Revokes customer's refresh token. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}