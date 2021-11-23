package org.lgajowy.sensordata.domain

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
        val newMin = this.min match {
          case None          => MinHumidity(newValue).some
          case Some(current) => MinHumidity(Math.min(current.value, newValue)).some
        }

        val newMax = this.max match {
          case None          => MaxHumidity(newValue).some
          case Some(current) => MaxHumidity(Math.max(current.value, newValue)).some
        }

        val newSum = this.sum match {
          case None          => SumHumidity(newValue).some
          case Some(current) => SumHumidity(current.value + newValue).some
        }

        this.copy(
          min = newMin,
          max = newMax,
          sum = newSum,
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
}

case class MinHumidity(value: Int)
case class MaxHumidity(value: Int)
case class SumHumidity(value: Int)
