package org.lgajowy.sensordata.domain

import cats.Monoid

case class MinHumidity(value: Int) extends AnyVal

object MinHumidity {
  implicit val monoidMinHumidity: Monoid[MinHumidity] = new Monoid[MinHumidity] {
    override def empty: MinHumidity = MinHumidity(0)
    override def combine(x: MinHumidity, y: MinHumidity): MinHumidity = MinHumidity(Math.min(x.value, y.value))
  }
}
