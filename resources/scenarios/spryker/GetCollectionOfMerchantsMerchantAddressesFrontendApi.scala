package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfMerchantsMerchantAddressesFrontendApiBase {

  lazy val scenarioName = "Retrieves merchant addresses."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/merchants/${merchant_reference}/merchant-addresses")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetCollectionOfMerchantsMerchantAddressesFrontendApiRamp extends Simulation with GetCollectionOfMerchantsMerchantAddressesFrontendApiBase {

  override lazy val scenarioName = "Retrieves merchant addresses. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfMerchantsMerchantAddressesFrontendApiSteady extends Simulation with GetCollectionOfMerchantsMerchantAddressesFrontendApiBase {

  override lazy val scenarioName = "Retrieves merchant addresses. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}