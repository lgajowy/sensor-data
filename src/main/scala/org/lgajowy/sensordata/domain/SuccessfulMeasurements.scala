package org.lgajowy.sensordata.domain

import cats.kernel.Monoid

case class SuccessfulMeasurements(value: Int) extends AnyVal {
  def increment() = SuccessfulMeasurements(value + 1)
}

object SuccessfulMeasurements {
  implicit val successfulMeasurementsMonoid: Monoid[SuccessfulMeasurements] = new Monoid[SuccessfulMeasurements] {
    override def empty: SuccessfulMeasurements = SuccessfulMeasurements(0)

    override def combine(x: SuccessfulMeasurements, y: SuccessfulMeasurements): SuccessfulMeasurements =
      SuccessfulMeasurements(x.value + y.value)
  }
}
