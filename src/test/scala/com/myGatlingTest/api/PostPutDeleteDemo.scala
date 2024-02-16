package com.myGatlingTest.api

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostPutDeleteDemo extends  Simulation{
  //protocol

  val httpProtocol = http
  .baseUrl("https://reqres.in/api")

//scenario
  val createuserscn =scenario(scenarioName = "create user")
    .exec(
      http(requestName = "create user request")
      .post(url = "/users")
      .header(name = "content-type", value = "application/json")
        .asJson
        .body(RawFileBody(filePath = "data/user.json"
        )).asJson
//        .body(StringBody(string = """{
//                                    |    "name": "morpheus",
//                                    |    "job": "leader"
//                                    |}""".stripMargin)).asJson

    .check(
      status is 201,
      jsonPath(path = "$.name") is "morpheus"
    )
    )
  pause(duration = 2)

  val updateuserscn =scenario(scenarioName = "update user")
    .exec(
      http(requestName = "update user request")
        .put("/users/2")
        .body(RawFileBody(filePath = "data/user.json"
        )).asJson
        .check(
          status is(200),
          jsonPath(path = "$.name") is "morpheus"

        )
    )
  pause(duration = 2)

  val deleteUserScn = scenario(scenarioName = "Delete user")
    .exec(
      http(requestName = "delete user request")
        .delete("/users/2")
        .check(status is 204)
    )
  //setup
  setUp(
    createuserscn.inject(rampUsers(users = 10).during(5)),
    updateuserscn.inject(rampUsers(users = 5).during(3)),
    deleteUserScn.inject(rampUsers(users = 3).during(2)),
  ).protocols(httpProtocol)




}
