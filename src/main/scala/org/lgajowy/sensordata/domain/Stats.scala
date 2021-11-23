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
  avg: AvgHumidity,
  min: MinHumidity,
  max: MaxHumidity
)
