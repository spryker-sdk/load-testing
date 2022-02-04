package spryker

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spryker.GlueProtocol._
import spryker.Scenario._
import spryker.CreateCiamTokenRequestApi._

trait CreateCustomersAddressesFrontendApiBase {

  lazy val scenarioName = "Creates customer address."

  val httpProtocol = GlueProtocol.httpProtocol

  val request = http(scenarioName)
    .post("/customers/${crmPk}/addresses")
    .body(StringBody("""{"data":{"type":"addresses","attributes":{"firstName":"${firstName}","lastName":"${lastName}","address1":"${billingAddress.address1}","zipCode":"${billingAddress.zipCode}","city":"${billingAddress.city}","regionIsoCode":"${billingAddress.regionIsoCode}","countryIsoCode":"${billingAddress.countryIsoCode}"}}}"""))
    .header("Authorization", "Bearer ${token}")
    .check(status.is(201))

  val scn = scenario(scenarioName)
    .exec(CreateCiamTokenRequestApi.executeRequest)
    .exec(request)
  }

class CreateCustomersAddressesFrontendApiRamp extends Simulation with CreateCustomersAddressesFrontendApiBase {

  override lazy val scenarioName = "Creates customer address. [Incremental]"

  setUp(scn.inject(
      rampUsersPerSec(1) to (Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(reachRps(Scenario.targetRps) in (Scenario.duration), holdFor(1 hour))
    .protocols(httpProtocol)
}

class CreateCustomersAddressesFrontendApiSteady extends Simulation with CreateCustomersAddressesFrontendApiBase {

  override lazy val scenarioName = "Creates customer address. [Steady RPS]"

  setUp(scn.inject(
      constantUsersPerSec(Scenario.targetRps.toDouble) during (Scenario.duration),
    ))
    .throttle(
      jumpToRps(Scenario.targetRps),
      holdFor(Scenario.duration),
    )
    .protocols(httpProtocol)
}