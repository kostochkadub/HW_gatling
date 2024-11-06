package vc

import io.gatling.core.Predef._
import io.gatling.core.structure._
import vc.Actions._
import scala.util.Random

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  def randomCityPair(cities: Seq[String]): (String, String) = {
    val departCity = cities(Random.nextInt(cities.length))
    val otherCities = cities.filter(_ != departCity)
    val arriveCity = otherCities(Random.nextInt(otherCities.length))
    (departCity, arriveCity)
  }

  val scn: ScenarioBuilder = scenario("Common scenario")
    .feed(csv("users.csv").circular)
    .exec(getMainPage)
    .exec(getNavPage)
    .exec(login)
    .exec(checkLoginWelcome)
    .exec(goToFlights)
    .exec(selectFlight)
    .exec { session =>
      val cities = session("cities").as[Seq[String]]
      println("Extracted cities: " + cities)
      val (departCity, arriveCity) = randomCityPair(cities)
//      println(s"Selected departCity: $departCity, arriveCity: $arriveCity")
      session.set("departCity", departCity).set("arriveCity", arriveCity)
    }
    .exec(findFlight)
    .exec { session =>
//      println("Response body: " + session("responseBody").as[String])
      session
    }
    .exec { session =>
      val outboundFlights = session("outboundFlights").as[Seq[String]]
//      println("Extracted outboundFlights: " + outboundFlights)
      if (outboundFlights.nonEmpty) {
        val randomOutboundFlight = outboundFlights(Random.nextInt(outboundFlights.length))
//        println("Selected outboundFlightValue: " + randomOutboundFlight)
        session.set("outboundFlightValue", randomOutboundFlight)
      } else {
//        println("No outbound flights found.")
        session.markAsFailed
      }
    }
    .exec(selectRandomFlight)
    .exec { session =>
//      println("Response body selectRandomFlight: " + session("responseBody1").as[String])
      session
    }
    .exec(purchaseTicket)
    .exec { session =>
//      println("Response body purchaseTicket: " + session("responseBody2").as[String])
      session
    }
    .exec(goToHomePage)
}