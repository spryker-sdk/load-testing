package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfMerchantCategoryTreesFrontendApiBase {

  lazy val scenarioName = "Retrieves category tree for specified merchant."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/merchant-category-trees")
    .header("Merchant-Reference", "${merchant_reference}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetCollectionOfMerchantCategoryTreesFrontendApiRamp extends Simulation with GetCollectionOfMerchantCategoryTreesFrontendApiBase {

  override lazy val scenarioName = "Retrieves category tree for specified merchant. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfMerchantCategoryTreesFrontendApiSteady extends Simulation with GetCollectionOfMerchantCategoryTreesFrontendApiBase {

  override lazy val scenarioName = "Retrieves category tree for specified merchant. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}