package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfCategoryTreesFrontendApiBase {

  lazy val scenarioName = "Retrieves category tree for specified locale."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/category-trees")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(request)
  }

class GetCollectionOfCategoryTreesFrontendApiRamp extends Simulation with GetCollectionOfCategoryTreesFrontendApiBase {

  override lazy val scenarioName = "Retrieves category tree for specified locale. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCategoryTreesFrontendApiSteady extends Simulation with GetCollectionOfCategoryTreesFrontendApiBase {

  override lazy val scenarioName = "Retrieves category tree for specified locale. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}