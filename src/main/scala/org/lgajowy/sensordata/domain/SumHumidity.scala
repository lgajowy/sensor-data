package org.lgajowy.sensordata.domain

import cats.Monoid

case class SumHumidity(value: Int) extends AnyVal

object SumHumidity {
  implicit val monoidSumHumidity: Monoid[SumHumidity] = new Monoid[SumHumidity] {
    override def empty: SumHumidity = SumHumidity(0)
    override def combine(x: SumHumidity, y: SumHumidity): SumHumidity = SumHumidity(x.value + y.value)
  }
}
