package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfConcreteProductsSalesUnitsFrontendApiBase {

  lazy val scenarioName = "Retrieves a collection of sales units."

  val httpProtocol = GlueProtocol.httpProtocol

  val product_concrete = csv("tests/_data/product_concrete.csv").random
  val request = http(scenarioName)
    .get("/concrete-products/${sku}/sales-units")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(product_concrete)
    .exec(request)
  }

class GetCollectionOfConcreteProductsSalesUnitsFrontendApiRamp extends Simulation with GetCollectionOfConcreteProductsSalesUnitsFrontendApiBase {

  override lazy val scenarioName = "Retrieves a collection of sales units. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfConcreteProductsSalesUnitsFrontendApiSteady extends Simulation with GetCollectionOfConcreteProductsSalesUnitsFrontendApiBase {

  override lazy val scenarioName = "Retrieves a collection of sales units. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}