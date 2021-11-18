package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.{CSVFilePath, DirectoryPath}

trait CsvFileFinder[F[_]] {
  def listCSVsInDirectory(directoryPath: DirectoryPath): F[List[CSVFilePath]]
}
