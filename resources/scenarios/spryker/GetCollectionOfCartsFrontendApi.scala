package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait GetCollectionOfCartsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of all customer's carts."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/carts?include=cart-rules&include=items&include=promotional-items&include=gift-cards&include=bundle-items&include=merchants")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCartsFrontendApiRamp extends Simulation with GetCollectionOfCartsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of all customer's carts. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCartsFrontendApiSteady extends Simulation with GetCollectionOfCartsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of all customer's carts. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}