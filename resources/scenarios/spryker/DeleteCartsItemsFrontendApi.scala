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

trait DeleteCartsItemsFrontendApiBase {

  lazy val scenarioName = "Removes item from the cart."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .delete("/carts/${cartId}/items/${sku}_${sku}_${merchant_reference}")
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(204))

  val scn = scenario(scenarioName)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
    .exec(AddToCartRequestApi.executeRequest)
    .exec(request)
  }

class DeleteCartsItemsFrontendApiRamp extends Simulation with DeleteCartsItemsFrontendApiBase {

  override lazy val scenarioName = "Removes item from the cart. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class DeleteCartsItemsFrontendApiSteady extends Simulation with DeleteCartsItemsFrontendApiBase {

  override lazy val scenarioName = "Removes item from the cart. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}