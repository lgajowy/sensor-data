package org.lgajowy.sensordata.domain

case class SuccessfulMeasurements(value: Int) {
  def increment() = SuccessfulMeasurements(value + 1)
}
