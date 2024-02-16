package com.myGatlingTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class feederDemo extends Simulation {

  //protocol

  val httpProtocol = http.baseUrl("https://computer-database.gatling.io")

  //scenario

  val feeder = csv(fileName = "data/usersFile.csv").circular
  val scn = scenario(scenarioName = "Feeder demo")
    .repeat( times = 1) {
    feed(feeder)
    .exec { session =>
      println("Name: " + session("name").as[String])
      println("Job: " + session("job").as[String])
      println("Page: " + session("page").as[String])
      session
    }
}
    .pause(duration = 2)
    .exec(http(requestName = "Go to ${page}")
    .get("/#{page}"))
//.get("/computers"))

  //setup

setUp(scn.inject(atOnceUsers(users = 3))).protocols(httpProtocol)


}
