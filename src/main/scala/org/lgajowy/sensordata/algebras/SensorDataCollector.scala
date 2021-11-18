package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.{ CsvFile, PerFileSensorData }

trait SensorDataCollector[F[_]] {
  def collectSensorData(filePath: CsvFile): F[List[PerFileSensorData]]
}
