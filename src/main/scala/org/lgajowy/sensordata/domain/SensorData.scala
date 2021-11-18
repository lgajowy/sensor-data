package org.lgajowy.sensordata.domain

case class SensorData(
  minHumidity: Integer,
  maxHumidity: Integer,
  processedSum: Integer,
  failedMeasurementsCount: Integer,
  processedMeasurementsCount: Integer
)
