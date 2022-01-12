package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._

trait GetCustomersFrontendApiBase {

  lazy val scenarioName = "Retrieves customer data."

  val httpProtocol = GlueProtocol.httpProtocol

  val customer = csv("tests/_data/customer.csv").random
  val request = http(scenarioName)
    .get("/customers/${customer_id}?include=addresses&include=merchants")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(customer)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class GetCustomersFrontendApiRamp extends Simulation with GetCustomersFrontendApiBase {

  override lazy val scenarioName = "Retrieves customer data. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCustomersFrontendApiSteady extends Simulation with GetCustomersFrontendApiBase {

  override lazy val scenarioName = "Retrieves customer data. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}