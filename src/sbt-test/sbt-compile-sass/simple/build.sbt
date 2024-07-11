lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    scalaVersion := "2.12.19",
    resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2"),
    libraryDependencies ++= Seq(
      "uk.gov.hmrc.webjars" % "hmrc-frontend" % "6.24.0"
    )
  )
