package org.lgajowy.sensordata.algebras

import cats.Functor
import cats.effect.Sync
import cats.implicits._
import fs2.io.file.{ Files, Flags, Path }
import fs2.{ Pipe, text }
import org.lgajowy.sensordata.domain._

trait SensorDataCollector[F[_]] {
  def collectSensorData(csvPath: CsvFilePath): F[PerFileSensorData]
}

object SensorDataCollector {
  def make[F[_]: Files: Sync: Functor](): SensorDataCollector[F] = new SensorDataCollector[F] {

    override def collectSensorData(csvPath: CsvFilePath): F[PerFileSensorData] = {

      case class Row(sensorId: SensorId, humidity: Option[Humidity])

      val parseRow: List[String] => Option[Row] = {
        case id :: value :: Nil if value.toLowerCase() == "nan" => Some(Row(SensorId(id), None))
        case id :: value :: Nil                                 => Some(Row(SensorId(id), Some(Humidity(value))))
        case _                                                  => None
      }

      def csvParser: Pipe[F, Byte, List[String]] =
        _.through(text.utf8.decode)
          .through(text.lines)
          .drop(1)
          .map(_.split(',').toList)

      Files[F]
        .readAll(
          Path.fromNioPath(csvPath.path),
          4096,
          Flags.Read
        )
        .through(csvParser)
        .map(parseRow)
        .unNoneTerminate
        .compile
        .fold(Map[SensorId, SensorData]()) { (acc: Map[SensorId, SensorData], row: Row) =>
          {

            val maybeSensorData: Option[SensorData] = acc.get(row.sensorId)
            val updatedSensorData: SensorData = maybeSensorData match {
              case Some(sensorData) => sensorData.update(row.humidity)
              case None             => SensorData(row.humidity)
            }

            acc + (row.sensorId -> updatedSensorData)
          }
        }
        .attempt
        .map {
          case Left(_)      => FailedFileRead(csvPath)
          case Right(value) => CorrectPerFileSensorData(csvPath, value)
        }
    }
  }
}
