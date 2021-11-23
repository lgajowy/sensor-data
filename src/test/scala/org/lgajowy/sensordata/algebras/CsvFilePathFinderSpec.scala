package org.lgajowy.sensordata.algebras

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.lgajowy.sensordata.domain.{ CantReadCsvFilesFromDirectory, CsvFilePath, DirectoryPath }
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.Paths

class CsvFilePathFinderSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  it should "read only csv files recursively" in {
    val finder = CsvFileFinder.make[IO]()

    finder
      .listCSVsInDirectory(DirectoryPath(Paths.get("src/test/resources/testDir")))
      .asserting(
        _ shouldBe
          List(
            CsvFilePath(Paths.get("src/test/resources/testDir/foo/bar.csv")),
            CsvFilePath(Paths.get("src/test/resources/testDir/foo/boo/csv.csv"))
          )
      )
  }

  it should "read no files when directory is empty" in {
    val finder = CsvFileFinder.make[IO]()
    finder
      .listCSVsInDirectory(DirectoryPath(Paths.get("src/test/resources/testDir/emptyDir")))
      .asserting(_ shouldBe List())
  }

  it should "fail to read a nonexistent directory" in {
    val finder = CsvFileFinder.make[IO]()
    finder
      .listCSVsInDirectory(DirectoryPath(Paths.get("src/test/resources/testDir/nosuchdir")))
      .assertThrows[CantReadCsvFilesFromDirectory]
  }
}
