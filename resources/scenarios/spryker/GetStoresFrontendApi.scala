package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetStoresFrontendApiBase {

  lazy val scenarioName = "Retrieves current store data."

  val httpProtocol = GlueProtocol.httpProtocol

  val stores = csv("tests/_data/stores.csv").random
  val request = http(scenarioName)
    .get("/stores/${store_id}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(stores)
    .exec(request)
  }

class GetStoresFrontendApiRamp extends Simulation with GetStoresFrontendApiBase {

  override lazy val scenarioName = "Retrieves current store data. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetStoresFrontendApiSteady extends Simulation with GetStoresFrontendApiBase {

  override lazy val scenarioName = "Retrieves current store data. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}