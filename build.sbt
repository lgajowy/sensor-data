scalaVersion := "2.13.7"

val Versions =
  new {
    val catsEffect = "3.2.9"
    val fs2 = "3.2.0"
    val scalatest = "3.1.4"
    val catsEffectTesting = "1.3.0"
  }

libraryDependencies ++= Seq(
  "co.fs2" %% "fs2-core" % Versions.fs2,
  "co.fs2" %% "fs2-io" % Versions.fs2,
  "org.typelevel" %% "cats-effect" % Versions.catsEffect,
  "org.scalatest" %% "scalatest" % Versions.scalatest % Test,
  "org.typelevel" %% "cats-effect-testing-scalatest" % Versions.catsEffectTesting % Test
)
