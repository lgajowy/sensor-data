package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.{PerFileSensorData, Stats}

trait SensorStatsCalculator[F[_]] {
  def calculate(allSensorData: List[PerFileSensorData]): F[Stats]
}
