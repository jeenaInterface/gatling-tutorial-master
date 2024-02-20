package com.myGatlingTest.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
class ComputerDataBase extends Simulation{

  //protocol

  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io")

  //scenario
  val search =
    exec(http("Home")
      .get("/"))
      .pause(2)
      .exec(http("Search")
        .get("/computers?f=macbook"))
      .pause(2)
      .exec(http("Select")
        .get("/computers/6"))
      .pause(2)

  val edit = exec(http(requestName ="Edit")
    .post("/computers/381")
    .formParam("name", "ACE")
    .formParam("introduced", "")
    .formParam("discontinued", "")
    .formParam("company", ""))
    .pause(12)


  val users = scenario("Users")
    .exec(search)
  val admins = scenario("Admins")
    .exec(search, edit)

  //setup
  setUp(
    users.inject(rampUsers(10).during(10)),
    admins.inject(rampUsers(2).during(10))
  ).protocols(httpProtocol)


}
