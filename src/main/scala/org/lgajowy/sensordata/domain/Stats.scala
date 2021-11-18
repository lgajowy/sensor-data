package org.lgajowy.sensordata.domain

case class Stats(
  failedMeasurements: FailedMeasurements,
  processedMeasurements: ProcessedMeasurements,
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
