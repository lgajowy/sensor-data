package org.lgajowy.sensordata.domain

case class Humidity(value: Int) extends AnyVal

object Humidity {
  def apply(value: String): Humidity = Humidity(value.toInt)
}
