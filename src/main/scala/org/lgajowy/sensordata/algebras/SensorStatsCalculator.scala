package org.lgajowy.sensordata.algebras

import cats.{ Applicative, Monoid }
import org.lgajowy.sensordata.domain._

trait SensorStatsCalculator[F[_]] {
  def calculate(allSensorData: List[PerFileSensorData]): F[Stats]
}

object SensorStatsCalculator {

  def make[F[_]: Applicative](): SensorStatsCalculator[F] = new SensorStatsCalculator[F] {

    private implicit val optionalAvgOrdering: Ordering[Option[AvgHumidity]] = new Ordering[Option[AvgHumidity]] {
      override def compare(x: Option[AvgHumidity], y: Option[AvgHumidity]): Int = {
        (x, y) match {
          case (Some(xVal), Some(yVal)) => if (xVal.value > yVal.value) 1 else if (xVal.value == yVal.value) 0 else -1
          case (None, Some(_))          => -1
          case (Some(_), None)          => 1
          case (None, None)             => 0
        }
      }
    }

    override def calculate(allSensorData: List[PerFileSensorData]): F[Stats] = Applicative[F].pure {
      val processedFiles = ProcessedFiles(allSensorData.size)
      val failedFiles = FailedFiles(allSensorData.count(_.isInstanceOf[FailedFileRead]))

      val correctFileReads: Seq[CorrectPerFileSensorData] = allSensorData
        .filter(_.isInstanceOf[CorrectPerFileSensorData])
        .asInstanceOf[List[CorrectPerFileSensorData]]

      val totalSensorDataPerSensor: Map[SensorId, SensorData] =
        correctFileReads
          .flatMap(_.data)
          .groupMapReduce(_._1)(_._2)(Monoid[SensorData].combine)

      val totalFailedMeasurements = totalSensorDataPerSensor.values
        .map(_.failedMeasurements)
        .fold(Monoid[FailedMeasurements].empty)(Monoid[FailedMeasurements].combine)

      val totalSuccessfulMeasurements = totalSensorDataPerSensor.values
        .map(_.successfulMeasurements)
        .fold(Monoid[SuccessfulMeasurements].empty)(Monoid[SuccessfulMeasurements].combine)

      val globalSensorStats = totalSensorDataPerSensor.map {
        case (id, data) => SensorStats(id, data.sum, data.min, data.max, data.successfulMeasurements)
      }.toList

      val sortedStats = globalSensorStats.sortBy(_.avg)(Ordering[Option[AvgHumidity]].reverse)

      Stats(
        totalFailedMeasurements,
        totalSuccessfulMeasurements,
        processedFiles,
        failedFiles,
        sortedStats
      )
    }
  }
}
