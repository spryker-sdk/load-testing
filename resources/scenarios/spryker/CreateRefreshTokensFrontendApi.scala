package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait CreateRefreshTokensFrontendApiBase {

  lazy val scenarioName = "Refreshes customer's auth token."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .post("/refresh-tokens")
    .body(StringBody("""{
  "data": {
    "type": "refresh-tokens",
    "attributes": {
      "refreshToken": "${refreshToken}"
    }
  }
}"""))
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class CreateRefreshTokensFrontendApiRamp extends Simulation with CreateRefreshTokensFrontendApiBase {

  override lazy val scenarioName = "Refreshes customer's auth token. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateRefreshTokensFrontendApiSteady extends Simulation with CreateRefreshTokensFrontendApiBase {

  override lazy val scenarioName = "Refreshes customer's auth token. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}