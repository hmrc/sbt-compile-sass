addSbtPlugin("com.github.sbt" % "sbt-web" % "1.5.8")

ThisBuild / organization := "uk.gov.hmrc"

lazy val `sbt-compile-sass` = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    scalaVersion := "2.12.19",
    libraryDependencies ++= Seq(
      "de.larsgrefer.sass" % "sass-embedded-host" % "3.6.0",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.12.0"
    ),
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )
