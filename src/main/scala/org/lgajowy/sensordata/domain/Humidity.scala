package org.lgajowy.sensordata.domain

case class Humidity(value: Int)

object Humidity {
  def apply(value: String): Humidity = Humidity(value.toInt)
}
