package org.lgajowy.sensordata.domain

case class FailedMeasurements(value: Int) {
  def increment() = FailedMeasurements(this.value + 1)
}
