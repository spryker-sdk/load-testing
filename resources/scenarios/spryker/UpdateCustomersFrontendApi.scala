package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateAccessTokenForExistingUserRequestApi._
import scala.util.Random

trait UpdateCustomersFrontendApiBase {

  lazy val scenarioName = "Updates customer data."

  val httpProtocol = GlueProtocol.httpProtocol

  val feeder = Iterator.continually(Map("last_name_suffix" -> (Random.alphanumeric.take(3).mkString)))

  val request = http(scenarioName)
    .patch("/customers/${customer_id}")
    .header("Authorization", "Bearer ${access_token}")
    .body(StringBody("""{
  "data": {
    "type": "customers",
    "attributes": {
      "firstName": "${first_name}",
      "lastName": "${last_name}_${last_name_suffix}",
      "gender": "Male",
      "salutation": "Mr",
      "email": "${email}",
      "password": "${password}",
      "confirmPassword": "${password}",
      "acceptedTerms": true
    }
  }
}"""))
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(CreateAccessTokenForExistingUserRequestApi.executeRequest)
    .exec(request)
  }

class UpdateCustomersFrontendApiRamp extends Simulation with UpdateCustomersFrontendApiBase {

  override lazy val scenarioName = "Updates customer data. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class UpdateCustomersFrontendApiSteady extends Simulation with UpdateCustomersFrontendApiBase {

  override lazy val scenarioName = "Updates customer data. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}