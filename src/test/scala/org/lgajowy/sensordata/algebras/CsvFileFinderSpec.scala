package org.lgajowy.sensordata.algebras

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.lgajowy.sensordata.domain.{ CantReadCsvFilesFromDirectory, CsvFile, DirectoryPath }
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import java.nio.file.Paths

class CsvFileFinderSpec extends AsyncFlatSpec with AsyncIOSpec with Matchers {

  it should "read only csv files recursively" in {
    val finder = CsvFileFinder.make[IO]()

    finder
      .listCSVsInDirectory(DirectoryPath(Paths.get("src/test/resources/testDir")))
      .asserting(
        _ shouldBe
          List(
            CsvFile(new File("src/test/resources/testDir/foo/bar.csv")),
            CsvFile(new File("src/test/resources/testDir/foo/boo/csv.csv"))
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
