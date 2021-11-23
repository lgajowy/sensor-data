package org.lgajowy.sensordata.algebras

import cats.MonadThrow
import cats.effect.Sync
import cats.syntax.all._
import org.lgajowy.sensordata.domain.{ CantReadCsvFilesFromDirectory, CsvFilePath, DirectoryPath }

import java.nio.file.{ Files, Path }
import scala.jdk.CollectionConverters.IteratorHasAsScala

trait CsvFileFinder[F[_]] {
  def listCSVsInDirectory(directoryPath: DirectoryPath): F[List[CsvFilePath]]
}

object CsvFileFinder {

  def make[F[_]: Sync: MonadThrow](): CsvFileFinder[F] = new CsvFileFinder[F] {
    override def listCSVsInDirectory(directoryPath: DirectoryPath): F[List[CsvFilePath]] =
      for {
        allFiles <- findAllFilesRecursively(directoryPath)
        filteredFiles = allFiles.filter((it: Path) => it.toString.toLowerCase.endsWith(".csv"))
        csvFiles = filteredFiles.map(CsvFilePath)
      } yield csvFiles

    def findAllFilesRecursively(directory: DirectoryPath): F[List[Path]] = {
      Sync[F]
        .delay(
          Files
            .walk(directory.path)
            .iterator()
            .asScala
            .filter(Files.isRegularFile(_))
            .toList
        )
        .adaptError {
          case _ => CantReadCsvFilesFromDirectory(directory)
        }
    }
  }
}
