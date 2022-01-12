package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfConcreteProductsProductOffersFrontendApiBase {

  lazy val scenarioName = "Retrieves a collection of product offers for product concrete."

  val httpProtocol = GlueProtocol.httpProtocol

  val product_concrete = csv("tests/_data/product_concrete.csv").random
  val request = http(scenarioName)
    .get("/concrete-products/${sku}/product-offers?merchantReference=474-001&include=merchants")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(product_concrete)
    .exec(request)
  }

class GetCollectionOfConcreteProductsProductOffersFrontendApiRamp extends Simulation with GetCollectionOfConcreteProductsProductOffersFrontendApiBase {

  override lazy val scenarioName = "Retrieves a collection of product offers for product concrete. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfConcreteProductsProductOffersFrontendApiSteady extends Simulation with GetCollectionOfConcreteProductsProductOffersFrontendApiBase {

  override lazy val scenarioName = "Retrieves a collection of product offers for product concrete. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}