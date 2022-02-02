package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfMerchantsMerchantOpeningHoursFrontendApiBase {

  lazy val scenarioName = "Retrieves merchant opening hours."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/merchants/${merchant_reference}/merchant-opening-hours")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetCollectionOfMerchantsMerchantOpeningHoursFrontendApiRamp extends Simulation with GetCollectionOfMerchantsMerchantOpeningHoursFrontendApiBase {

  override lazy val scenarioName = "Retrieves merchant opening hours. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfMerchantsMerchantOpeningHoursFrontendApiSteady extends Simulation with GetCollectionOfMerchantsMerchantOpeningHoursFrontendApiBase {

  override lazy val scenarioName = "Retrieves merchant opening hours. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}