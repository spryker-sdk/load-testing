package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCheckoutRequestApi._

trait GetCollectionOfCustomerPaymentMethodsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of customer payment methods."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/customers/${customer_id}/customer-payment-methods")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCheckoutRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCustomerPaymentMethodsFrontendApiRamp extends Simulation with GetCollectionOfCustomerPaymentMethodsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of customer payment methods. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCustomerPaymentMethodsFrontendApiSteady extends Simulation with GetCollectionOfCustomerPaymentMethodsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of customer payment methods. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}