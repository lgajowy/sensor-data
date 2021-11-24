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

case class MinHumidity(value: Int)

object MinHumidity {
  implicit val monoidMinHumidity: Monoid[MinHumidity] = new Monoid[MinHumidity] {
    override def empty: MinHumidity = MinHumidity(0)
    override def combine(x: MinHumidity, y: MinHumidity): MinHumidity = MinHumidity(Math.min(x.value, y.value))
  }
}
case class MaxHumidity(value: Int)

object MaxHumidity {
  implicit val monoidMaxHumidity: Monoid[MaxHumidity] = new Monoid[MaxHumidity] {
    override def empty: MaxHumidity = MaxHumidity(0)
    override def combine(x: MaxHumidity, y: MaxHumidity): MaxHumidity = MaxHumidity(Math.max(x.value, y.value))
  }
}

case class SumHumidity(value: Int)

object SumHumidity {
  implicit val monoidSumHumidity: Monoid[SumHumidity] = new Monoid[SumHumidity] {
    override def empty: SumHumidity = SumHumidity(0)
    override def combine(x: SumHumidity, y: SumHumidity): SumHumidity = SumHumidity(x.value + y.value)
  }
}
