package org.lgajowy.sensordata.domain

import scala.util.control.NoStackTrace

sealed trait Error extends NoStackTrace {
  def message: String
}

case class CantReadDirectory(path: DirectoryPath) extends Error {
  override val message: String = s"Directory ${path.path} could not be read"
}
