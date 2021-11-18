package org.lgajowy.sensordata.domain

sealed trait Error {
  def message: String
}

case class CantReadDirectory(path: DirectoryPath) extends Error {
  override val message: String = s"Directory ${path.path} could not be read"
}
