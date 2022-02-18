ThisBuild / scalaVersion     := "3.1.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "quill-2",
    libraryDependencies ++= Seq(
      "io.getquill"   %% "quill-jdbc-zio" % "3.16.3.Beta2.4",
      "org.postgresql" % "postgresql"     % "42.3.1",
      "dev.zio"       %% "zio"            % "1.0.12",
      "dev.zio"       %% "zio-test"       % "1.0.12" % Test
    ),
    scalacOptions ++= Seq("-Ymacro-annotations"),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
