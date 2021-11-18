package org.lgajowy.sensordata.domain

sealed trait PerFileSensorData

case class FailedFileRead(csvFile: CsvFile) extends PerFileSensorData

case class CorrectPerFileSensorData(
  csvFile: CsvFile,
  data: Map[SensorId, SensorData]
)
