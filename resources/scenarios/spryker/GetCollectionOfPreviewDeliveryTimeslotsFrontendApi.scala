package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._

trait GetCollectionOfPreviewDeliveryTimeslotsFrontendApiBase {

  lazy val scenarioName = "Retrieves list of merchant delivery timeslots for preview."

  val httpProtocol = GlueProtocol.httpProtocol

  val merchants = csv("tests/_data/merchants.csv").random
  val request = http(scenarioName)
    .get("/preview-delivery-timeslots?merchantReference=${merchant_reference}&zipCode=32836")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .feed(merchants)
    .exec(request)
  }

class GetCollectionOfPreviewDeliveryTimeslotsFrontendApiRamp extends Simulation with GetCollectionOfPreviewDeliveryTimeslotsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of merchant delivery timeslots for preview. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class GetCollectionOfPreviewDeliveryTimeslotsFrontendApiSteady extends Simulation with GetCollectionOfPreviewDeliveryTimeslotsFrontendApiBase {

  override lazy val scenarioName = "Retrieves list of merchant delivery timeslots for preview. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}