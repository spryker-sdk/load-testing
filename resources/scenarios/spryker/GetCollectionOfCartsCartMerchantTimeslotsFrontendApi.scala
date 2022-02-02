package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCustomerRequestApi._
import spryker.CreateAccessTokenRequestApi._
import spryker.CreateCartRequestApi._
import spryker.AddToCartRequestApi._

trait GetCollectionOfCartsCartMerchantTimeslotsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of cart merchant timeslots for selection."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .get("/carts/${cartId}/cart-merchant-timeslots")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
    .exec(AddToCartRequestApi.executeRequest)
    .exec(request)
  }

class GetCollectionOfCartsCartMerchantTimeslotsFrontendApiRamp extends Simulation with GetCollectionOfCartsCartMerchantTimeslotsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of cart merchant timeslots for selection. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfCartsCartMerchantTimeslotsFrontendApiSteady extends Simulation with GetCollectionOfCartsCartMerchantTimeslotsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of cart merchant timeslots for selection. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}