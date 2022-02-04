package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCiamTokenRequestApi._

trait GetCollectionOfCustomersAddressesFrontendApiBase {

  lazy val scenarioName = "Retrieves list of all customer addresses."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/customers/${crmPk}/addresses")
    .header("Authorization", "Bearer ${token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCiamTokenRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCustomersAddressesFrontendApiRamp extends Simulation with GetCollectionOfCustomersAddressesFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of all customer addresses. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCustomersAddressesFrontendApiSteady extends Simulation with GetCollectionOfCustomersAddressesFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of all customer addresses. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}