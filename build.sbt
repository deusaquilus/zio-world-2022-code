ThisBuild / scalaVersion     := "3.1.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "quill-2",
    libraryDependencies ++= Seq(
      "io.getquill"           %% "quill-jdbc-zio"    % "3.16.3.Beta2.4",
      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "org.postgresql"         % "postgresql"        % "42.3.1",
      "dev.zio"               %% "zio"               % "1.0.12",
      "dev.zio"               %% "zio-test"          % "1.0.12" % Test,
      "com.github.ghostdogpr" %% "caliban"           % "1.3.3",
      "com.github.ghostdogpr" %% "caliban-zio-http"  % "1.3.3",
      "dev.zio"               %% "zio-json"          % "0.2.0-M3" // update to 0.3.0-RC3 for zio2 support
    ),
    scalacOptions ++= Seq("-Ymacro-annotations"),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
