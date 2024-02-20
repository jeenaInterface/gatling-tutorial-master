package com.myGatlingTest.api

import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.Predef._
import java.io.FileWriter
import scala.io.Source

class compareTwoFiles extends Simulation{

  val httpProtocol = http.baseUrl("https://reqres.in")

  val scn = scenario("Write Response to JSON File")
    .exec(http("GET Request")
      .get("/api/users/2")
      .check(status.is(200))
      .check(bodyString.saveAs("responseBody")))

    // Write the response body to a JSON file
    .exec(session => {
      val responseBody = session("responseBody").as[String] // Retrieve the saved response body
      val filePath = getClass.getClassLoader.getResource("data/Original.json").getPath
      try {
        val fileWriter = new FileWriter(filePath)
        try {
          fileWriter.write(responseBody)
        } finally {
          fileWriter.close()
        }
        println("JSON written to file successfully.")
      } catch {
        case e: Exception => println(s"Error writing JSON to file: ${e.getMessage}")
      }
      session // Return the session as is
    })

  // Read the first JSON file
  val jsonFile1: String = Source.fromResource("data/FirstJson.json").mkString

  // Read the second JSON file
  val jsonFile2: String = Source.fromResource("data/Original.json").mkString

  // Define the scenario for comparing the data
  val comparisonScenario = scenario("JSON Data Comparison")
    .exec { session =>
      // Print the content of the second JSON file
     println("Content of the second JSON file: $jsonFile2")

      // Compare the data from the two JSON files
      if (jsonFile1 == jsonFile2) {
        println("Data in the two JSON files is identical")
      } else {
        println("Data in the two JSON files is different")
      }
      session
    }

  // Define the load simulation
  setUp(
    scn.inject(atOnceUsers(10))
      .protocols(httpProtocol)
  )
}
