ThisBuild / scalaVersion     := "3.1.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "quill-2",
    resolvers ++= Seq(
      Resolver.mavenLocal,
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
    ),
    libraryDependencies ++= Seq(
      "io.getquill"           %% "quill-jdbc-zio"   % "3.17.0.Beta3.0-RC2",
      "ch.qos.logback"         % "logback-classic"  % "1.2.10",
      "org.postgresql"         % "postgresql"       % "42.3.1",
      "dev.zio"               %% "zio"              % "2.0.0-RC2",
      "dev.zio"               %% "zio-test"         % "2.0.0-RC2" % Test,
      "com.github.ghostdogpr" %% "caliban"          % "2.0.0-RC1",
      "com.github.ghostdogpr" %% "caliban-zio-http" % "2.0.0-RC1",
      "dev.zio"               %% "zio-json"         % "0.3.0-RC3", // update to 0.3.0-RC3 for zio2 support
      "io.d11"                %% "zhttp"            % "2.0.0-RC4"
    ),
    scalacOptions ++= Seq("-Ymacro-annotations"),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
