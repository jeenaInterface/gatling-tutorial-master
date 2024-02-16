package com.myGatlingTest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ComputerDataBaseSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")

	val headers_0 = Map(
		"sec-ch-ua" -> """Not A(Brand";v="99", "Google Chrome";v="121", "Chromium";v="121""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "same-origin",
		"sec-fetch-user" -> "?1")

	val headers_2 = Map(
		"cache-control" -> "max-age=0",
		"origin" -> "https://computer-database.gatling.io",
		"sec-ch-ua" -> """Not A(Brand";v="99", "Google Chrome";v="121", "Chromium";v="121""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows",
		"sec-fetch-dest" -> "document",
		"sec-fetch-mode" -> "navigate",
		"sec-fetch-site" -> "same-origin",
		"sec-fetch-user" -> "?1")



	val search = exec(http(requestName ="Search")
			.get("/computers?f=ACE")
			.headers(headers_0))
		.pause(1)
		.exec(http("select")
			.get("/computers/381")
			.headers(headers_0))
		.pause(5)

val edit = exec(http(requestName ="Edit")
			.post("/computers/381")
			.headers(headers_2)
			.formParam("name", "ACE")
			.formParam("introduced", "")
			.formParam("discontinued", "")
			.formParam("company", ""))
		.pause(12)

	val scn = scenario(scenarioName = "SearchAndEditComputer").exec(search,edit)

	val users = scenario(scenarioName = "Users").exec(search)
	val admins = scenario(scenarioName = "Admins").exec(search,edit)

	setUp(
		users.inject(rampUsers(users = 10).during(10)) ,
		admins.inject(rampUsers(users = 4).during(10))
	).protocols(httpProtocol)
}