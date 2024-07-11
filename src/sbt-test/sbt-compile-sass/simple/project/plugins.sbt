sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("uk.gov.hmrc" % "sbt-compile-sass" % x)
  case _ => addSbtPlugin("uk.gov.hmrc" % "sbt-compile-sass" % "0.1.0-SNAPSHOT")
//  case _ => sys.error("""|The system property 'plugin.version' is not defined.
//                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
