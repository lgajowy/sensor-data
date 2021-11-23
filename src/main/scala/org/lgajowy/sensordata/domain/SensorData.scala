package org.lgajowy.sensordata.domain

import cats.Monoid
import cats.implicits.catsSyntaxOptionId

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
          min = MinHumidity.getSmaller(this.min, Some(MinHumidity(newValue))),
          max = MaxHumidity.getGreater(this.max, Some(MaxHumidity(newValue))),
          sum = SumHumidity.getSum(this.sum, Some(SumHumidity(newValue))),
          this.failedMeasurements,
          this.successfulMeasurements.increment()
        )
    }
  }
}

object SensorData {
  def apply(humidity: Option[Humidity]): SensorData = {
    humidity match {
      case None =>
        SensorData(
          None,
          None,
          None,
          FailedMeasurements(1),
          SuccessfulMeasurements(0)
        )
      case Some(humidity) =>
        SensorData(
          MinHumidity(humidity.value).some,
          MaxHumidity(humidity.value).some,
          SumHumidity(humidity.value).some,
          FailedMeasurements(0),
          SuccessfulMeasurements(1)
        )
    }
  }

  implicit val sensorDataMonoid: Monoid[SensorData] = new Monoid[SensorData] {
    override def empty: SensorData = SensorData(
      None,
      None,
      None,
      FailedMeasurements(0),
      SuccessfulMeasurements(0)
    )

    override def combine(x: SensorData, y: SensorData): SensorData = SensorData(
      MinHumidity.getSmaller(x.min, y.min),
      MaxHumidity.getGreater(x.max, y.max),
      SumHumidity.getSum(x.sum, y.sum),
      Monoid[FailedMeasurements].combine(x.failedMeasurements, y.failedMeasurements),
      Monoid[SuccessfulMeasurements].combine(x.successfulMeasurements, y.successfulMeasurements)
    )
  }
}

case class MinHumidity(value: Int)

object MinHumidity {
  def getSmaller(x: Option[MinHumidity], y: Option[MinHumidity]): Option[MinHumidity] = {
    (x, y) match {
      case (Some(xVal), Some(yVal)) => Some(MinHumidity(Math.min(xVal.value, yVal.value)))
      case (Some(value), None)      => Some(value)
      case (None, Some(value))      => Some(value)
      case (None, None)             => None
    }
  }
}
case class MaxHumidity(value: Int)

object MaxHumidity {
  def getGreater(x: Option[MaxHumidity], y: Option[MaxHumidity]): Option[MaxHumidity] = {
    (x, y) match {
      case (Some(xVal), Some(yVal)) => Some(MaxHumidity(Math.max(xVal.value, yVal.value)))
      case (Some(value), None)      => Some(value)
      case (None, Some(value))      => Some(value)
      case (None, None)             => None
    }
  }
}

case class SumHumidity(value: Int)

object SumHumidity {
  def getSum(x: Option[SumHumidity], y: Option[SumHumidity]): Option[SumHumidity] = {
    (x, y) match {
      case (Some(xVal), Some(yVal)) => Some(SumHumidity(xVal.value + yVal.value))
      case (Some(value), None)      => Some(value)
      case (None, Some(value))      => Some(value)
      case (None, None)             => None
    }
  }
}
