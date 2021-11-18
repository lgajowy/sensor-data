package org.lgajowy.sensordata.domain

import scala.util.control.NoStackTrace

sealed trait Error extends NoStackTrace {
  def message: String
}

case class CantReadCsvFilesFromDirectory(path: DirectoryPath) extends Error {
  override val message: String = s"Csv files from path: ${path.path} could not be read"
}

case class MissingDirectoryPathArgument(currentArgs: List[String]) extends Error {
  override val message: String = s"Can't find the directory path command line argument. Current args: $currentArgs"
}

case class InvalidPath(path: String) extends Error {
  override val message: String = s"This is not a valid directory path: $path"
}

case class NotADirectoryPath(path: String) extends Error {
  override val message: String = s"This is not a directory path: $path"
}

case class NoSuchDirectory(path: String) extends Error {
  override val message: String = s"No such directory: $path"
}
