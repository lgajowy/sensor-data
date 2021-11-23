package org.lgajowy.sensordata.programs

import cats.effect.ExitCode
import cats.syntax.all._
import cats.{MonadThrow, Parallel}
import org.lgajowy.sensordata.algebras._

final case class Program[F[_]: MonadThrow: Parallel](
  commandLineArgumentsParser: CommandLineArgumentsParser[F],
  csvFileFinder: CsvFileFinder[F],
  sensorDataCollector: SensorDataCollector[F],
  sensorStatsCalculator: SensorStatsCalculator[F],
  statsPrinter: StatsPrinter[F]
) {
  def run(args: List[String]): F[ExitCode] = {
    for {
      directory <- commandLineArgumentsParser.parseDirectoryPath(args)
      files <- csvFileFinder.listCSVsInDirectory(directory)
      perFileResults <- files.parTraverse(sensorDataCollector.collectSensorData)
      stats <- sensorStatsCalculator.calculate(perFileResults)
      _ <- statsPrinter.print(stats)
    } yield ExitCode.Success
  }
}
