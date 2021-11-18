package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.{CSVFilePath, PerFileSensorData}

trait SensorDataCollector[F[_]] {
  def collectSensorData(filePath: CSVFilePath): F[List[PerFileSensorData]]
}
