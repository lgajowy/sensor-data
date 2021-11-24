package org.lgajowy.sensordata.program

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.lgajowy.sensordata.algebras._
import org.lgajowy.sensordata.programs.Program
import org.lgajowy.sensordata.testutils.TestConsole
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

class ProgramSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  it should "read a directory and print the aggregated result" in {

    val testConsole: TestConsole = new TestConsole()

    val program = Program[IO](
      CommandLineArgumentsParser.make[IO](),
      CsvFileFinder.make[IO](),
      SensorDataCollector.make[IO](),
      SensorStatsCalculator.make[IO](),
      StatsPrinter.make[IO](testConsole)
    )

    program
      .run(List("src/test/resources/csvExamples"))
      .asserting(
        _ =>
          testConsole.lines shouldBe Vector(
            "Num of processed files: 3",
            "Num of failed files: 0",
            "Num of processed measurements: 12",
            "Num of failed measurements: 6",
            "Sensors with highest avg humidity:",
            "sensor-id,min,avg,max",
            "s2,1,62,88",
            "s1,10,54,98",
            "s3,NaN,NaN,NaN"
          )
      )
  }

}
