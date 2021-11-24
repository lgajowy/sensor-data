package org.lgajowy.sensordata.algebras

import cats.Show
import cats.effect.IO
import cats.effect.std.Console
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.catsSyntaxOptionId
import org.lgajowy.sensordata.domain._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.charset.Charset

class StatsPrinterSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  class TestConsole extends Console[IO] {

    var lines: Vector[String] = Vector[String]()

    override def readLineWithCharset(charset: Charset): IO[String] = ???

    override def print[A](a: A)(implicit S: Show[A]): IO[Unit] = ???

    override def println[A](a: A)(implicit S: Show[A]): IO[Unit] = IO.pure {
      lines = lines ++ Vector(Show[A].show(a))
    }

    override def error[A](a: A)(implicit S: Show[A]): IO[Unit] = ???

    override def errorln[A](a: A)(implicit S: Show[A]): IO[Unit] = ???
  }

  it should "print the given stats correctly" in {

    val testConsole: TestConsole = new TestConsole()
    val printer = StatsPrinter.make[IO](testConsole)

    printer
      .print(
        Stats(
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
              SensorId("s3"),
              None,
              None,
              None
            )
          )
        )
      )
      .asserting(_ => {
        testConsole.lines shouldBe List(
          "Num of processed files: 3",
          "Num of failed files: 1",
          "Num of processed measurements: 6",
          "Num of failed measurements: 2",
          "Sensors with highest avg humidity:",
          "sensor-id,min,avg,max",
          "s2,10,13,15",
          "s3,NaN,NaN,NaN"
        )
      })

  }

}
