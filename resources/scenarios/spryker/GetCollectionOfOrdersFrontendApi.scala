package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCheckoutRequestApi._

trait GetCollectionOfOrdersFrontendApiBase {

  lazy val scenarioName = "Retrieves list of orders."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/orders")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCheckoutRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfOrdersFrontendApiRamp extends Simulation with GetCollectionOfOrdersFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of orders. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfOrdersFrontendApiSteady extends Simulation with GetCollectionOfOrdersFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of orders. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}