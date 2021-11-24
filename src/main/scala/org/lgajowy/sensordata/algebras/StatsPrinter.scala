package org.lgajowy.sensordata.algebras

import cats.effect.Sync
import cats.effect.std.Console
import cats.implicits._
import org.lgajowy.sensordata.domain.{ SensorStats, Stats }

trait StatsPrinter[F[_]] {
  def print(stats: Stats): F[Unit]
}

object StatsPrinter {
  def make[F[_]: Sync](console: Console[F]) = new StatsPrinter[F] {

    override def print(stats: Stats): F[Unit] = Sync[F].delay {

      console.println(s"Num of processed files: ${stats.processedFiles.value}")
      console.println(s"Num of failed files: ${stats.failedFiles.value}")
      console.println(
        s"Num of processed measurements: ${stats.successfulMeasurements.value + stats.failedMeasurements.value}"
      )
      console.println(s"Num of failed measurements: ${stats.failedMeasurements.value}")
      console.println("Sensors with highest avg humidity:")
      console.println("sensor-id,min,avg,max")
      stats.sensorsByAvgHumidity.foreach((line: SensorStats) => console.println(formatSensorStatsString(line)))
    }

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
