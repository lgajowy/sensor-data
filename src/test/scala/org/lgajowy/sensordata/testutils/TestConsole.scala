package org.lgajowy.sensordata.testutils

import cats.Show
import cats.effect.IO
import cats.effect.std.Console

import java.nio.charset.Charset

class TestConsole extends Console[IO] {

  var lines: Vector[String] = Vector[String]()

  override def readLineWithCharset(charset: Charset): IO[String] = ???

  override def print[A](a: A)(implicit S: Show[A]): IO[Unit] = ???

  override def println[A](a: A)(implicit S: Show[A]): IO[Unit] = IO.pure {
    lines = lines ++ Vector(Show[A].show(a))
  }

  override def error[A](a: A)(implicit S: Show[A]): IO[Unit] = ???

  override def errorln[A](a: A)(implicit S: Show[A]): IO[Unit] = ???
}
