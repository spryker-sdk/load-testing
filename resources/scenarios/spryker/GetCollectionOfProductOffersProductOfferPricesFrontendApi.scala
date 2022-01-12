package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfProductOffersProductOfferPricesFrontendApiBase {

  lazy val scenarioName = "Retrieves collection of product-offer-prices."

  val httpProtocol = GlueProtocol.httpProtocol

  val product_concrete = csv("tests/_data/product_concrete.csv").random
  val request = http(scenarioName)
    .get("/product-offers/${product_offer_reference}/product-offer-prices")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(product_concrete)
    .exec(request)
  }

class GetCollectionOfProductOffersProductOfferPricesFrontendApiRamp extends Simulation with GetCollectionOfProductOffersProductOfferPricesFrontendApiBase {

  override lazy val scenarioName = "Retrieves collection of product-offer-prices. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfProductOffersProductOfferPricesFrontendApiSteady extends Simulation with GetCollectionOfProductOffersProductOfferPricesFrontendApiBase {

  override lazy val scenarioName = "Retrieves collection of product-offer-prices. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}