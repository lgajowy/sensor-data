package org.lgajowy.sensordata.algebras

import cats.effect.Sync
import cats.syntax.all._
import org.lgajowy.sensordata.domain._

import java.nio.file.{ Files, Paths }

trait CommandLineArgumentsParser[F[_]] {
  def parseDirectoryPath(args: List[String]): F[DirectoryPath]
}

object CommandLineArgumentsParser {
  def make[F[_]: Sync](): CommandLineArgumentsParser[F] = {
    case args @ Nil => MissingDirectoryPathArgument(args).raiseError
    case firstArg :: _ =>
      Paths.get(firstArg) match {
        case path if !Files.exists(path)      => NoSuchDirectory(firstArg).raiseError
        case path if !Files.isDirectory(path) => NotADirectoryPath(firstArg).raiseError
        case path                             => Sync[F].delay(DirectoryPath(path))
      }
  }
}
