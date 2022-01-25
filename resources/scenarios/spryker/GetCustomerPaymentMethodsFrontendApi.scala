package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCheckoutRequestApi._

trait GetCustomerPaymentMethodsFrontendApiBase {

  lazy val scenarioName = "Retrieves customer payment method by id."

  val httpProtocol = GlueProtocol.httpProtocol

  val customerPaymentMethodsRequest = http("Retrieves list of customer payment methods.")
    .get("/customers/${customer_id}/customer-payment-methods")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))
    .check(bodyString.saveAs("customerPaymentMethodsRequest"))
    .check(jsonPath("$.data[0].id").saveAs("customer_payment_method_id"))

  val request = http(scenarioName)
    .get("/customers/${customer_id}/customer-payment-methods/${customer_payment_method_id}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCheckoutRequestApi.executeRequest)
    .exec(customerPaymentMethodsRequest)
      .exec(session => {
      println("customerPaymentMethodsRequest")
      println(session("customerPaymentMethodsRequest").as[String])
      println("customer_payment_method_id")
      println(session("customer_payment_method_id").as[String])
      session
    })
    // .exec(request)
  }

class GetCustomerPaymentMethodsFrontendApiRamp extends Simulation with GetCustomerPaymentMethodsFrontendApiBase {

  override lazy val scenarioName = "Retrieves customer payment method by id. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (3),
    ))
    .throttle(reachRps(Scenario.targetRps) in (3), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCustomerPaymentMethodsFrontendApiSteady extends Simulation with GetCustomerPaymentMethodsFrontendApiBase {

  override lazy val scenarioName = "Retrieves customer payment method by id. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (3),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(3),
    )
    .protocols(httpProtocol)
}