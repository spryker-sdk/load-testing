package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfHealthCheckServicesFrontendApiBase {

  lazy val scenarioName = "Retrieve collection of health check services."

  val httpProtocol = GlueProtocol.httpProtocol

  val services = csv("tests/_data/services.csv").random
  val request = http(scenarioName)
    .get("/health-check")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(services)
    .exec(request)
  }

class GetCollectionOfHealthCheckServicesFrontendApiRamp extends Simulation with GetCollectionOfHealthCheckServicesFrontendApiBase {

  override lazy val scenarioName = "Retrieve collection of health check services. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfHealthCheckServicesFrontendApiSteady extends Simulation with GetCollectionOfHealthCheckServicesFrontendApiBase {

  override lazy val scenarioName = "Retrieve collection of health check services. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}