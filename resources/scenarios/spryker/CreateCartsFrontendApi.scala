package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCustomerRequestApi._
import spryker.CreateAccessTokenRequestApi._
import spryker.CreateCartRequestApi._

trait CreateCartsFrontendApiBase {

  lazy val scenarioName = "Creates a cart."

  val httpProtocol = GlueProtocol.httpProtocol

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
  }

class CreateCartsFrontendApiRamp extends Simulation with CreateCartsFrontendApiBase {

  override lazy val scenarioName = "Creates a cart. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCartsFrontendApiSteady extends Simulation with CreateCartsFrontendApiBase {

  override lazy val scenarioName = "Creates a cart. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}