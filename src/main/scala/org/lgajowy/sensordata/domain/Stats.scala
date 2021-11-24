package org.lgajowy.sensordata.domain

case class Stats(
  failedMeasurements: FailedMeasurements,
  successfulMeasurements: SuccessfulMeasurements,
  processedFiles: ProcessedFiles,
  failedFiles: FailedFiles,
  sensorsByAvgHumidity: List[SensorStats]
)

case class SensorStats(
  id: SensorId,
  avg: Option[AvgHumidity],
  min: Option[MinHumidity],
  max: Option[MaxHumidity]
)

object SensorStats {
  def apply(
    id: SensorId,
    sum: Option[SumHumidity],
    min: Option[MinHumidity],
    max: Option[MaxHumidity],
    successfulMeasurements: SuccessfulMeasurements
  ): SensorStats = SensorStats(
    id,
    sum.map(it => AvgHumidity((it.value.toDouble / successfulMeasurements.value).round.toInt)),
    min,
    max
  )
}
