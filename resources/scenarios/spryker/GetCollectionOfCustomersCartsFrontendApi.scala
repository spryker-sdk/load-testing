package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait GetCollectionOfCustomersCartsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of carts."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/customers/${customer_id}/carts")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCustomersCartsFrontendApiRamp extends Simulation with GetCollectionOfCustomersCartsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of carts. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCustomersCartsFrontendApiSteady extends Simulation with GetCollectionOfCustomersCartsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of carts. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}