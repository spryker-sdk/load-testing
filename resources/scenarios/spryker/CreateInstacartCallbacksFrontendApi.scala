package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCheckoutRequestApi._
import scala.util.Random
import spryker.Encryptor._
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait CreateInstacartCallbacksFrontendApiBase {

  lazy val scenarioName = "Process callback from the Instacart."

  val httpProtocol = GlueProtocol.httpProtocol

  val eventId = Instant.now.getEpochSecond
  val eventTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(LocalDateTime.now)
  val feeder = Array(
    StringBody("""{"event_name":"fulfillment.acknowledged","event_metadata":{"order_id":"${order_reference}"},"event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.picking","event_metadata":{"order_id":"${order_reference}","order_url":"retailer.com/xyz/picking/orderurl","store_location":"${merchant_reference}"},"event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.late_delivery","event_metadata":{"order_id":"${order_reference}","new_window":{"starts_at":"2022-05-06 07:30:00","ends_at":"2022-05-06 09:00:00"}},"event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.late_pickup","event_id":"""" + eventId + """","event_metadata":{"order_id":"${order_reference}","new_window":{"starts_at":"2021-06-06 08:30:00","ends_at":"2021-05-06 13:15:00"}},"event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.rescheduled","event_metadata":{"order_id":"${order_reference}","new_window":{"starts_at":"2021-06-20 07:50:00","ends_at":"2021-06-20 09:30:00"}},"event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.canceled","event_metadata":{"order_id":"${order_reference}","cancellation_reason":"this is test cancellation reason"},"event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """"}"""),
    StringBody("""{"event_name":"fulfillment.staged","event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """","event_metadata":{"order_id":"${order_reference}","status_link":"delivery.retailer.com/xyz","order_items":[{"line_num":"${sku}_${product_offer_reference}","qty":1,"qty_unit":"each","replaced":false,"refunded":true,"item_upc":"${merchant_reference}","item_rrc":"","scan_code":"8743521"}],"pickup_link":"retailer.com/xyz1/pickuplink"}}"""),
    StringBody("""{"event_name":"fulfillment.delivering","event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """","event_metadata":{"order_id":"${order_reference}","order_items":[{"line_num":"${sku}_${product_offer_reference}","qty":1,"qty_unit":"each","replaced":false,"refunded":false,"item_upc":"","item_rrc":"0000000000001177","scan_code":"8743528"}],"delivery_window":{"starts_at":"2022-07-30 07:50:00","ends_at":"2022-07-30 09:30:00"}}}"""),
    StringBody("""{"event_name":"fulfillment.delivered","event_id":"""" + eventId + """","event_timestamp":"""" + eventTimestamp + """","event_metadata":{"order_id":"${order_reference}","order_items":[{"line_num":"${sku}_${product_offer_reference}","qty":1,"qty_unit":"each","replaced":false,"refunded":false,"item_upc":"","item_rrc":"0000000000001177","scan_code":"8743528"}]}}"""),
  )

  val getAccessTokenRequest = http("Create token.")
    .post("/token")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .formParam("client_id", "01a7afdeff08543006f109184b733912")
    .formParam("client_secret", "instacart-password")
    .formParam("grant_type", "client_credentials")
    .check(status.is(200))
    .check(jsonPath("$.access_token").saveAs("access_token"))

  val request = http(scenarioName)
    .post("/instacart-callbacks")
    .body(Random.shuffle(feeder.toList).head)
    .header("Authorization", "Bearer ${access_token}")
    .check(status.is(200))

  val scn = scenario(scenarioName)
    .exec(CreateCheckoutRequestApi.executeRequest)
    .exec(getAccessTokenRequest)
    .exec(request)
  }

class CreateInstacartCallbacksFrontendApiRamp extends Simulation with CreateInstacartCallbacksFrontendApiBase {

  override lazy val scenarioName = "Process callback from the Instacart. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (5),
    ))
    .throttle(reachRps(Scenario.targetRps) in (5), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateInstacartCallbacksFrontendApiSteady extends Simulation with CreateInstacartCallbacksFrontendApiBase {

  override lazy val scenarioName = "Process callback from the Instacart. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (5),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(5),
    )
    .protocols(httpProtocol)
}