package org.lgajowy.sensordata.domain

import cats.Monoid

case class MaxHumidity(value: Int)

object MaxHumidity {
  implicit val monoidMaxHumidity: Monoid[MaxHumidity] = new Monoid[MaxHumidity] {
    override def empty: MaxHumidity = MaxHumidity(0)
    override def combine(x: MaxHumidity, y: MaxHumidity): MaxHumidity = MaxHumidity(Math.max(x.value, y.value))
  }
}
