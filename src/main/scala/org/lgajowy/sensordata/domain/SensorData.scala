package org.lgajowy.sensordata.domain

case class SensorData(
  min: MinHumidity,
  max: MaxHumidity,
  sum: HumiditySum,
  failedMeasurements: FailedMeasurements,
  processedMeasurements: ProcessedMeasurements
)

case class MinHumidity(value: Int)
case class MaxHumidity(value: Int)
case class HumiditySum(value: Int)
