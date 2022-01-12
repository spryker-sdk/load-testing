package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetMerchantsFrontendApiBase {

  lazy val scenarioName = "Retrieves a merchant by id."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/merchants/${merchant_reference}?include=merchant-addresses&include=merchant-opening-hours")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetMerchantsFrontendApiRamp extends Simulation with GetMerchantsFrontendApiBase {

  override lazy val scenarioName = "Retrieves a merchant by id. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetMerchantsFrontendApiSteady extends Simulation with GetMerchantsFrontendApiBase {

  override lazy val scenarioName = "Retrieves a merchant by id. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}