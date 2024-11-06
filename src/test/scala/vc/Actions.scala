package vc

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object Actions {

//супер сайт отдает везде 200 всегда
//лучше проверить, что пользователь еще валиден, перед запуском
  val getMainPage: HttpRequestBuilder = http("getMainPage")
    .get("/WebTours/")
    .check(status is 200)

  val getNavPage: HttpRequestBuilder = http("getNavPage")
    .get("/cgi-bin/nav.pl?in=home")
    .check(status is 200)
    .check(regex("""<input type="hidden" name="userSession" value="([^"]+)"""").saveAs("userSession"))

  val login: HttpRequestBuilder = http("login")
    .post("/cgi-bin/login.pl")
    .formParam("userSession", "#{userSession}")
    .formParam("username", "#{login}")
    .formParam("password", "#{password}")
    .formParam("login.x", "57")
    .formParam("login.y", "13")
    .check(status is 200)

  val checkLoginWelcome: HttpRequestBuilder = http("checkLoginWelcome")
    .get("/cgi-bin/login.pl?intro=true")
    .check(status is 200)
    .check(substring("Welcome").exists)  // Проверка на наличие слова "Welcome" в ответе

  val goToFlights: HttpRequestBuilder = http("goToFlights")
    .get("/cgi-bin/welcome.pl?page=search")
    .check(status is 200)

  val selectFlight: HttpRequestBuilder = http("selectFlight")
    .get("/cgi-bin/reservations.pl?page=welcome")
    .check(status is 200)
    .check(css("""select[name="depart"] option""", "value").findAll.saveAs("cities"))


  val findFlight: HttpRequestBuilder = http("chooseFlight")
    .post("/cgi-bin/reservations.pl")
    .formParam("advanceDiscount", "0")
    .formParam("depart", "#{departCity}")
    .formParam("departDate", "11/10/2024") // можно установить актуальную дату
    .formParam("arrive", "#{arriveCity}")
    .formParam("returnDate", "11/17/2024") // и ткт установить актуальную дату + пара дней
    .formParam("numPassengers", "1")
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("findFlights.x", "42")
    .formParam("findFlights.y", "5")
    .formParam(".cgifields", "roundtrip")
    .formParam(".cgifields", "seatType")
    .formParam(".cgifields", "seatPref")
    .check(status is 200)
    .check(bodyString.saveAs("responseBody"))
    .check(css("""input[name="outboundFlight"]""", "value").findAll.saveAs("outboundFlights"))


  val selectRandomFlight: HttpRequestBuilder = http("selectRandomFlight")
    .post("/cgi-bin/reservations.pl")
    .formParam("outboundFlight", "#{outboundFlightValue}")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("advanceDiscount", "0")
    .formParam("reserveFlights.x", "56")
    .formParam("reserveFlights.y", "4")
    .check(status is 200)
    .check(bodyString.saveAs("responseBody1"))

  val purchaseTicket: HttpRequestBuilder = http("purchaseTicket")
    .post("/cgi-bin/reservations.pl")
    .formParam("firstName", "dummyName")
    .formParam("lastName", "dummyLastName")
    .formParam("address1", "dummyStreetAddress ")
    .formParam("address2", "dummyCity")
    .formParam("creditCard", "dummyCreditCard")
    .formParam("expDate", "dummyExpD")
    .formParam("advanceDiscount", "0")
    .formParam("outboundFlight", "#{outboundFlightValue}")
    .formParam("numPassengers", "1")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("buyFlights.x", "48")
    .formParam("buyFlights.y", "14")
    .formParam("saveCC", "on")
    .formParam(".cgifields", "saveCC")
    .check(status is 200)
    .check(bodyString.saveAs("responseBody2"))

  val goToHomePage: HttpRequestBuilder = http("goToHomePage")
    .get("/cgi-bin/nav.pl?page=menu&in=home")
    .check(status is 200)
}
