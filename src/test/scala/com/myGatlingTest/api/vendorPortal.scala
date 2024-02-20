package com.myGatlingTest.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
class vendorPortal extends Simulation{

  // Define HTTP protocol configuration
  val httpProtocol = http
    .baseUrl("https://vendorportal.interfacesys.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .disableCaching

  // Define the scenario for authentication
  val authenticationScenario = scenario("Authentication Scenario")
    .exec(http("Open Login Page")
      .get("/Account/Login.aspx"))
    .pause(1 second)
    .exec(http("Submit Credentials")
      .post("/Account/Login.aspx")
      .formParam("username", "michael.eppenberger") // Replace with your actual username
      .formParam("password", "Interface01!") // Replace with your actual password
      .check(status.is(200)))

  // Define the load simulation
  setUp(
    authenticationScenario.inject(atOnceUsers(10)) // Adjust the number of users as needed
  ).protocols(httpProtocol)
}
