package org.lgajowy.sensordata

import cats.effect.{ ExitCode, IO, IOApp }
import org.lgajowy.sensordata.algebras._
import org.lgajowy.sensordata.programs.Program

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    Program[IO](
      CommandLineArgumentsParser.make[IO](),
      CsvFileFinder.make[IO](),
      SensorDataCollector.make[IO](),
      SensorStatsCalculator.make[IO](),
      StatsPrinter.make[IO](IO.consoleForIO)
    ).run(args)
}
