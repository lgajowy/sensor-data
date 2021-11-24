package org.lgajowy.sensordata.domain

import cats.kernel.Monoid

case class FailedMeasurements(value: Int) extends AnyVal {
  def increment() = FailedMeasurements(this.value + 1)
}

object FailedMeasurements {
  implicit val failedMeasurementsMonoid: Monoid[FailedMeasurements] = new Monoid[FailedMeasurements] {
    override def empty: FailedMeasurements = FailedMeasurements(0)

    override def combine(x: FailedMeasurements, y: FailedMeasurements): FailedMeasurements =
      FailedMeasurements(x.value + y.value)
  }
}
