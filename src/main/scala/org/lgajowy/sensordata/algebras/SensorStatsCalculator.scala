package org.lgajowy.sensordata.algebras

import cats.{ Applicative, Monoid }
import org.lgajowy.sensordata.domain._

trait SensorStatsCalculator[F[_]] {
  def calculate(allSensorData: List[PerFileSensorData]): F[Stats]
}

object SensorStatsCalculator {

  def make[F[_]: Applicative](): SensorStatsCalculator[F] = new SensorStatsCalculator[F] {

    override def calculate(allSensorData: List[PerFileSensorData]): F[Stats] = Applicative[F].pure {
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

      val sensorStats = totalSensorDataPerSensor
        .map { case (id, data) => SensorStats(id, data.sum, data.min, data.max, data.successfulMeasurements) }
        .toList
        .sortBy(_.avg)(Ordering[Option[AvgHumidity]].reverse)

      Stats(
        totalFailedMeasurements,
        totalSuccessfulMeasurements,
        ProcessedFiles(allSensorData.size),
        FailedFiles(allSensorData.count(_.isInstanceOf[FailedFileRead])),
        sensorStats
      )
    }
  }
}
