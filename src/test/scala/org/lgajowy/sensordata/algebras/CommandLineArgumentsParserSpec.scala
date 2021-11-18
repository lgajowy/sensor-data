package org.lgajowy.sensordata.algebras

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.lgajowy.sensordata.domain.{ DirectoryPath, MissingDirectoryPathArgument, NotADirectoryPath }
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.Paths

class CommandLineArgumentsParserSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  final val ExpectedPathString = "src/test/resources/testDir/foo"
  final val ExpectedPath = Paths.get(ExpectedPathString)

  it should "read first command line arg as the path" in {
    val parser = CommandLineArgumentsParser.make[IO]()

    parser
      .parseDirectoryPath(List(ExpectedPathString))
      .asserting(_ shouldBe DirectoryPath(ExpectedPath))
  }

  it should "return an Error in case the arg list is empty" in {
    val parser = CommandLineArgumentsParser.make[IO]()

    parser
      .parseDirectoryPath(List())
      .assertThrows[MissingDirectoryPathArgument]
  }

  it should "allow more arguments to be passed as long the first one is a valid directory path" in {
    val parser = CommandLineArgumentsParser.make[IO]()

    parser
      .parseDirectoryPath(List(ExpectedPathString, "foo", "1"))
      .assertNoException
  }

  "provided path" should "not be a path to a file" in {
    val parser = CommandLineArgumentsParser.make[IO]()

    parser
      .parseDirectoryPath(List("src/test/resources/testDir/foo/baz.txt"))
      .assertThrows[NotADirectoryPath]
  }
}
