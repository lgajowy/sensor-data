package org.lgajowy.sensordata.algebras

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.catsSyntaxOptionId
import org.lgajowy.sensordata.domain.{ CorrectPerFileSensorData, _ }
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.Paths

class SensorStatsCalculatorSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  it should "calculate stats globally given results from multiple files" in {
    val calculator = SensorStatsCalculator.make[IO]()

    calculator
      .calculate(
        List(
          CorrectPerFileSensorData(
            CsvFilePath(Paths.get("foo")),
            Map(
              SensorId("s1") -> SensorData(
                MinHumidity(10).some,
                MaxHumidity(10).some,
                SumHumidity(10).some,
                FailedMeasurements(1),
                SuccessfulMeasurements(1)
              ),
              SensorId("s2") -> SensorData(
                MinHumidity(15).some,
                MaxHumidity(15).some,
                SumHumidity(30).some,
                FailedMeasurements(0),
                SuccessfulMeasurements(2)
              )
            )
          ),
          FailedFileRead(
            CsvFilePath(Paths.get("bar"))
          ),
          CorrectPerFileSensorData(
            CsvFilePath(Paths.get("baz")),
            Map(
              SensorId("s2") -> SensorData(
                MinHumidity(10).some,
                MaxHumidity(10).some,
                SumHumidity(10).some,
                FailedMeasurements(0),
                SuccessfulMeasurements(1)
              ),
              SensorId("s3") -> SensorData(
                None,
                None,
                None,
                FailedMeasurements(1),
                SuccessfulMeasurements(0)
              )
            )
          )
        )
      )
      .asserting(
        _ shouldBe Stats(
          FailedMeasurements(2),
          SuccessfulMeasurements(4),
          ProcessedFiles(3),
          FailedFiles(1),
          List(
            SensorStats(
              SensorId("s2"),
              AvgHumidity(13).some,
              MinHumidity(10).some,
              MaxHumidity(15).some
            ),
            SensorStats(
              SensorId("s1"),
              AvgHumidity(10).some,
              MinHumidity(10).some,
              MaxHumidity(10).some
            ),
            SensorStats(
              SensorId("s3"),
              None,
              None,
              None
            )
          )
        )
      )
  }

  it should "round not using floor but using 0.5 as the rounding point" in {
    val calculator = SensorStatsCalculator.make[IO]()

    calculator
      .calculate(
        List(
          CorrectPerFileSensorData(
            CsvFilePath(Paths.get("foo")),
            Map(
              SensorId("s1") -> SensorData(
                MinHumidity(3).some,
                MaxHumidity(3).some,
                SumHumidity(3).some,
                FailedMeasurements(0),
                SuccessfulMeasurements(2)
              ),
              SensorId("s2") -> SensorData(
                MinHumidity(2).some,
                MaxHumidity(2).some,
                SumHumidity(2).some,
                FailedMeasurements(0),
                SuccessfulMeasurements(3)
              )
            )
          )
        )
      )
      .asserting(result => {
        result.sensorsByAvgHumidity.head.avg shouldBe AvgHumidity(2).some
        result.sensorsByAvgHumidity.last.avg shouldBe AvgHumidity(1).some
      })

  }
}
