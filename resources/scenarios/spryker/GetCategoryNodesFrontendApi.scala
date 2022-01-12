package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCategoryNodesFrontendApiBase {

  lazy val scenarioName = "Retrieves a category node by id."

  val httpProtocol = GlueProtocol.httpProtocol

  val category_nodes = csv("tests/_data/category_nodes.csv").random
  val request = http(scenarioName)
    .get("/category-nodes/1")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(category_nodes)
    .exec(request)
  }

class GetCategoryNodesFrontendApiRamp extends Simulation with GetCategoryNodesFrontendApiBase {

  override lazy val scenarioName = "Retrieves a category node by id. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCategoryNodesFrontendApiSteady extends Simulation with GetCategoryNodesFrontendApiBase {

  override lazy val scenarioName = "Retrieves a category node by id. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}