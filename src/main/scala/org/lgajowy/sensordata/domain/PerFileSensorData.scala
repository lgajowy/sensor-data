package org.lgajowy.sensordata.domain

sealed trait PerFileSensorData

case class FailedFileRead(csvFilePath: CSVFilePath) extends PerFileSensorData

case class CorrectPerFileSensorData(
  csvFilePath: CSVFilePath,
  data: Map[SensorId, SensorData]
)
