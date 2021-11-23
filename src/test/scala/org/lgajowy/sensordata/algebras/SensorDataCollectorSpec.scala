package org.lgajowy.sensordata.algebras

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.catsSyntaxOptionId
import org.lgajowy.sensordata.domain._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.Paths

class SensorDataCollectorSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  it should "read a csv" in {
    val collector = SensorDataCollector.make[IO]()
    val file = CsvFilePath(Paths.get("src/test/resources/csvExamples/example1.csv"))

    collector
      .collectSensorData(file)
      .asserting {
        _ shouldBe CorrectPerFileSensorData(
          file,
          Map(
            SensorId("s1") -> SensorData(
              MinHumidity(10).some,
              MaxHumidity(10).some,
              SumHumidity(10).some,
              FailedMeasurements(1),
              SuccessfulMeasurements(1)
            ),
            SensorId("s2") -> SensorData(
              MinHumidity(1).some,
              MaxHumidity(88).some,
              SumHumidity(89).some,
              FailedMeasurements(0),
              SuccessfulMeasurements(2)
            )
          )
        )
      }
  }

  it should "return no min max and sum results since all records are NaN" in {
    val collector = SensorDataCollector.make[IO]()
    val file = CsvFilePath(Paths.get("src/test/resources/csvExamples/exampleNaNOnly.csv"))

    collector
      .collectSensorData(file)
      .asserting {
        _ shouldBe CorrectPerFileSensorData(
          file,
          Map(
            SensorId("s1") -> SensorData(
              None,
              None,
              None,
              FailedMeasurements(1),
              SuccessfulMeasurements(0)
            ),
            SensorId("s2") -> SensorData(
              None,
              None,
              None,
              FailedMeasurements(2),
              SuccessfulMeasurements(0)
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
      }
  }

  it should "return a failed parsing result when there was an exception" in {
    val collector = SensorDataCollector.make[IO]()
    val file = CsvFilePath(Paths.get("src/test/resources/csvExamples/nonexistingFile.csv"))

    collector
      .collectSensorData(file)
      .asserting(_ shouldBe FailedFileRead(file))
  }
}
