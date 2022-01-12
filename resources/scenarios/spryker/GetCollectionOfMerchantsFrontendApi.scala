package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfMerchantsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of merchants."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/merchants?include=merchant-addresses&include=merchant-opening-hours")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(request)
  }

class GetCollectionOfMerchantsFrontendApiRamp extends Simulation with GetCollectionOfMerchantsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of merchants. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfMerchantsFrontendApiSteady extends Simulation with GetCollectionOfMerchantsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of merchants. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}