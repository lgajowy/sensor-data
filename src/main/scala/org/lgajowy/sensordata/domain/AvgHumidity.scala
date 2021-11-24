package org.lgajowy.sensordata.domain

case class AvgHumidity(value: Int) extends AnyVal

object AvgHumidity {
  implicit val ordering: Ordering[AvgHumidity] = (x: AvgHumidity, y: AvgHumidity) => x.value - y.value
}
