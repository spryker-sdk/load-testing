package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApiBase {

  lazy val scenarioName = "Retrieves concrete product image sets."

  val httpProtocol = GlueProtocol.httpProtocol

  val product_concrete = csv("tests/_data/product_concrete.csv").random
  val request = http(scenarioName)
    .get("/concrete-products/${sku}/concrete-product-image-sets")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(product_concrete)
    .exec(request)
  }

class GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApiRamp extends Simulation with GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApiBase {

  override lazy val scenarioName = "Retrieves concrete product image sets. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApiSteady extends Simulation with GetCollectionOfConcreteProductsConcreteProductImageSetsFrontendApiBase {

  override lazy val scenarioName = "Retrieves concrete product image sets. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}