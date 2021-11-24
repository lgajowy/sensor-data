package org.lgajowy.sensordata.algebras

import cats.{Applicative, Monad}
import cats.effect.Sync
import cats.effect.std.Console
import cats.implicits._
import org.lgajowy.sensordata.domain.{SensorStats, Stats}

trait StatsPrinter[F[_]] {
  def print(stats: Stats): F[Unit]
}

object StatsPrinter {
  def make[F[_]: Sync](console: Console[F]) = new StatsPrinter[F] {

    override def print(stats: Stats): F[Unit] =
      for {
        _ <- console.println(s"Num of processed files: ${stats.processedFiles.value}")
        _ <- console.println(s"Num of failed files: ${stats.failedFiles.value}")
        _ <- console.println(
          s"Num of processed measurements: ${stats.successfulMeasurements.value + stats.failedMeasurements.value}"
        )
        _ <- console.println(s"Num of failed measurements: ${stats.failedMeasurements.value}")
        _ <- console.println("Sensors with highest avg humidity:")
        _ <- console.println("sensor-id,min,avg,max")
        allStatsLines = stats.sensorsByAvgHumidity.map(formatSensorStatsString)
        _ <- allStatsLines.traverse(console.println(_))
      } yield ()

    private def formatSensorStatsString(sensorStats: SensorStats): String = {
      val NaNStringValue = "NaN"
      List(
        sensorStats.id.id,
        sensorStats.min.map(_.value).getOrElse(NaNStringValue),
        sensorStats.avg.map(_.value).getOrElse(NaNStringValue),
        sensorStats.max.map(_.value).getOrElse(NaNStringValue)
      ).mkString(",")
    }
  }
}
