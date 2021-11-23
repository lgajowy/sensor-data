package org.lgajowy.sensordata.domain

sealed trait PerFileSensorData

case class FailedFileRead(csvFile: CsvFilePath) extends PerFileSensorData

case class CorrectPerFileSensorData(
  csvFile: CsvFilePath,
  data: Map[SensorId, SensorData]
) extends PerFileSensorData
