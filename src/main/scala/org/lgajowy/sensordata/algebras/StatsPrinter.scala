package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.Stats

trait StatsPrinter[F[_]] {
  def print(stats: Stats): F[Unit]
}
