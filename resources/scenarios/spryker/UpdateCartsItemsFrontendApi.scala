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
import scala.util.Random

trait UpdateCartsItemsFrontendApiBase {

  lazy val scenarioName = "Updates cart item quantity."

  val httpProtocol = GlueProtocol.httpProtocol

  val feeder = Iterator.continually(Map("quantity" -> (Random.nextInt(20))))

  val request = http(scenarioName)
    .patch("/carts/${cartId}/items/${sku}_${sku}_${merchant_reference}")
    .body(StringBody("""{"data":{"type":"items","attributes":{"sku":"${sku}","quantity":${quantity},"idPromotionalItem":"string","productOfferReference":"${product_offer_reference}","merchantReference":"${merchant_reference}","salesUnit":{"id":0,"amount":55},"productOptions":[{}],"isReplaceable":true}}}""")).asJson
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(feeder)
    .exec(CreateCustomerRequestApi.executeRequest)
    .exec(CreateAccessTokenRequestApi.executeRequest)
    .exec(CreateCartRequestApi.executeRequest)
    .exec(AddToCartRequestApi.executeRequest)
    .exec(request)
  }

class UpdateCartsItemsFrontendApiRamp extends Simulation with UpdateCartsItemsFrontendApiBase {

  override lazy val scenarioName = "Updates cart item quantity. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class UpdateCartsItemsFrontendApiSteady extends Simulation with UpdateCartsItemsFrontendApiBase {

  override lazy val scenarioName = "Updates cart item quantity. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}