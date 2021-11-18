package org.lgajowy.sensordata.algebras

import cats.MonadThrow
import cats.effect.Sync
import cats.syntax.all._
import org.lgajowy.sensordata.domain.{ CantReadCsvFilesFromDirectory, CsvFile, DirectoryPath }

import java.io.File
import java.nio.file.Files
import scala.jdk.CollectionConverters.IteratorHasAsScala

trait CsvFileFinder[F[_]] {
  def listCSVsInDirectory(directoryPath: DirectoryPath): F[List[CsvFile]]
}

object CsvFileFinder {

  def make[F[_]: Sync: MonadThrow](): CsvFileFinder[F] = new CsvFileFinder[F] {
    override def listCSVsInDirectory(directoryPath: DirectoryPath): F[List[CsvFile]] =
      for {
        allFiles <- findAllFilesRecursively(directoryPath)
        filteredFiles = allFiles.filter((it: File) => it.getPath.endsWith(".csv"))
        csvFiles = filteredFiles.map(CsvFile)
      } yield csvFiles

    def findAllFilesRecursively(directory: DirectoryPath): F[List[File]] = {
      Sync[F]
        .delay(
          Files
            .walk(directory.path)
            .iterator()
            .asScala
            .filter(Files.isRegularFile(_))
            .map(_.toFile)
            .toList
        )
        .adaptError {
          case _ => CantReadCsvFilesFromDirectory(directory)
        }
    }
  }
}
