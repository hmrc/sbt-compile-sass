lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    scalaVersion := "2.12.19",
    resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2"),
    libraryDependencies ++= Seq(
      "uk.gov.hmrc.webjars" % "hmrc-frontend" % "6.24.0"
    ),
    TaskKey[Unit]("check") := {
      val sourceSass = (Assets / WebKeys.public).value / "stylesheets" / "test.scss"
      if (sourceSass.exists) {
        sys.error("The source sass file was copied into target/web/public")
      }

      val compiledCSS = IO.read((Assets / WebKeys.public).value / "stylesheets" / "test.css")
      if (compiledCSS.contains("@import")) {
        sys.error("The compiled css did not resolve and inline it's @import's")
      }
    }
  )
