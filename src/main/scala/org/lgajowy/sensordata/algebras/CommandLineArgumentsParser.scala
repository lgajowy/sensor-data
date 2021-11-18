package org.lgajowy.sensordata.algebras

import org.lgajowy.sensordata.domain.DirectoryPath

trait CommandLineArgumentsParser[F[_]] {

  def parseDirectoryPath(args: List[String]): F[DirectoryPath]
}
