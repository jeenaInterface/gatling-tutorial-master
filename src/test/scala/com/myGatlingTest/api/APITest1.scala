package com.myGatlingTest.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class APITest1 extends Simulation {
  //protocol
  val httpProtocol = http
    .baseUrl("https://reqres.in/api/users")

//scenario
val scn = scenario(scenarioName = "Get API Request Demo")
  .exec(http(requestName = "Get single user")
    .get("/2")
    .check(
      status.is(expected = 200),
      jsonPath(path = "$.data.first_name").is(expected = "Janet"))
  )
  //setup
setUp(
  scn.inject(atOnceUsers( users = 10)).protocols(httpProtocol)
)

}
