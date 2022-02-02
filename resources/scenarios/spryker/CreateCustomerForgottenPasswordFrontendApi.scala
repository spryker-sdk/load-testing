package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait CreateCustomerForgottenPasswordFrontendApiBase {

  lazy val scenarioName = "Sends password restoration email."

  val httpProtocol = GlueProtocol.httpProtocol

  val customer = csv("tests/_data/customer.csv").random
  val request = http(scenarioName)
    .post("/customer-forgotten-password")
    .body(StringBody("""{
  "data": {
    "type": "customer-forgotten-password",
    "attributes": {
      "email": "${email}"
    }
  }
}"""))
    .check(status.is(204))

  val scn = scenario(scenarioName)
    .feed(customer)
    .exec(request)
  }

class CreateCustomerForgottenPasswordFrontendApiRamp extends Simulation with CreateCustomerForgottenPasswordFrontendApiBase {

  override lazy val scenarioName = "Sends password restoration email. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCustomerForgottenPasswordFrontendApiSteady extends Simulation with CreateCustomerForgottenPasswordFrontendApiBase {

  override lazy val scenarioName = "Sends password restoration email. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}