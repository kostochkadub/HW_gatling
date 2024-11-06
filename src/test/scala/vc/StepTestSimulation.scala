package vc

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open.OpenInjectionStep

import scala.concurrent.duration.DurationInt

class StepTestSimulation extends Simulation {

  val maxRps = 10 // Заменить на найденную максимальную производительность

  val steps: Seq[OpenInjectionStep] = (1 to 10).map { i =>
    val targetRps = (maxRps * i * 0.1)
    println("targetRps: " + targetRps)
    constantUsersPerSec(targetRps).during(2.minutes)
  }

//  val steps: Seq[OpenInjectionStep] = (1 to 10).flatMap { i =>
//    val startRps = (maxRps * (i - 1) * 0.1)
//    val endRps = (maxRps * i * 0.1)
//    Seq(
//      rampUsersPerSec(startRps).to(endRps).during(1.minute),
//      constantUsersPerSec(endRps).during(1.minute)
//    )
//  }

  setUp(
    CommonScenario().inject(
      steps
    )
  ).protocols(httpProtocol)
    .assertions(
      global.failedRequests.percent.lt(5), // Процент ошибок меньше 5%
      global.responseTime.mean.lt(1000)    // Среднее время отклика меньше 1000 мс
    )
}
