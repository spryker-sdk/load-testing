package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetProductMeasurementUnitsFrontendApiBase {

  lazy val scenarioName = "Retrieves product measurement unit by code."

  val httpProtocol = GlueProtocol.httpProtocol

  val measurement_units = csv("tests/_data/measurement_units.csv").random
  val request = http(scenarioName)
    .get("/product-measurement-units/${product_measurement_unit_id}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(measurement_units)
    .exec(request)
  }

class GetProductMeasurementUnitsFrontendApiRamp extends Simulation with GetProductMeasurementUnitsFrontendApiBase {

  override lazy val scenarioName = "Retrieves product measurement unit by code. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetProductMeasurementUnitsFrontendApiSteady extends Simulation with GetProductMeasurementUnitsFrontendApiBase {

  override lazy val scenarioName = "Retrieves product measurement unit by code. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}