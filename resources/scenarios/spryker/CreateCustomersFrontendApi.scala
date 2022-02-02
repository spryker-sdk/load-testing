package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCustomerRequestApi._

trait CreateCustomersFrontendApiBase {

  lazy val scenarioName = "Creates customer."

  val httpProtocol = GlueProtocol.httpProtocol

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
  }

class CreateCustomersFrontendApiRamp extends Simulation with CreateCustomersFrontendApiBase {

  override lazy val scenarioName = "Creates customer. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCustomersFrontendApiSteady extends Simulation with CreateCustomersFrontendApiBase {

  override lazy val scenarioName = "Creates customer. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}