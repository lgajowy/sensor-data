package org.lgajowy.sensordata.domain

import cats.Monoid
import cats.implicits.{ catsSyntaxOptionId, catsSyntaxSemigroup }

case class SensorData(
  min: Option[MinHumidity],
  max: Option[MaxHumidity],
  sum: Option[SumHumidity],
  failedMeasurements: FailedMeasurements,
  successfulMeasurements: SuccessfulMeasurements
) {
  def update(humidity: Option[Humidity]): SensorData = {
    humidity match {
      case None => this.copy(failedMeasurements = failedMeasurements.increment())
      case Some(Humidity(newValue)) =>
        this.copy(
          this.min |+| MinHumidity(newValue).some,
          this.max |+| MaxHumidity(newValue).some,
          this.sum |+| SumHumidity(newValue).some,
          this.failedMeasurements,
          this.successfulMeasurements.increment()
        )
    }
  }
}

object SensorData {
  def apply(humidity: Option[Humidity]): SensorData = {
    humidity match {
      case None    => Monoid[SensorData].empty.update(None)
      case Some(_) => Monoid[SensorData].empty.update(humidity)
    }
  }

  implicit val sensorDataMonoid: Monoid[SensorData] = new Monoid[SensorData] {
    override def empty: SensorData = SensorData(
      None,
      None,
      None,
      Monoid[FailedMeasurements].empty,
      Monoid[SuccessfulMeasurements].empty
    )

    override def combine(x: SensorData, y: SensorData): SensorData = SensorData(
      x.min |+| y.min,
      x.max |+| y.max,
      x.sum |+| y.sum,
      x.failedMeasurements |+| y.failedMeasurements,
      x.successfulMeasurements |+| y.successfulMeasurements
    )
  }
}
