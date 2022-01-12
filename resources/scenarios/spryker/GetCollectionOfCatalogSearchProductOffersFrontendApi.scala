package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfCatalogSearchProductOffersFrontendApiBase {

  lazy val scenarioName = "Catalog search product offers."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/catalog-search-product-offers?merchantReference=${merchant_reference}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetCollectionOfCatalogSearchProductOffersFrontendApiRamp extends Simulation with GetCollectionOfCatalogSearchProductOffersFrontendApiBase {

  override lazy val scenarioName = "Catalog search product offers. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCatalogSearchProductOffersFrontendApiSteady extends Simulation with GetCollectionOfCatalogSearchProductOffersFrontendApiBase {

  override lazy val scenarioName = "Catalog search product offers. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}