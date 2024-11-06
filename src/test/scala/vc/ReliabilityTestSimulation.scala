package vc

import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class ReliabilityTestSimulation extends Simulation {

  val maxRps = 100 // Заменить на найденную максимальную производительность
  val targetRps = (maxRps * 0.8).toInt

  setUp(
    CommonScenario().inject(
      constantUsersPerSec(targetRps).during(1.hour)
    )
  ).protocols(httpProtocol)
}
