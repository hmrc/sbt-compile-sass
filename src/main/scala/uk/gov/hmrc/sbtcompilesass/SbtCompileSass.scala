package uk.gov.hmrc.sbtcompilesass

import sbt.*
import sbt.Keys.*
import com.typesafe.sbt.web.SbtWeb.autoImport.*
import com.typesafe.sbt.web.Import.WebKeys.*
import com.typesafe.sbt.web.*
import de.larsgrefer.sass.embedded.SassCompilerFactory
import scala.collection.JavaConverters._

import java.nio.file.Files
import scala.util.Using

/*
  This compiles sass in the same way as sbt-sassify but using dart-sass

  Unlike SbtSassify this does not do incremental compilation, is that only about speed (maybe not needed)
  or is it also about recompiling when changes are made in development?
 */
object SbtCompileSass extends AutoPlugin {
  override def requires: Plugins      = SbtWeb
  override def trigger: PluginTrigger = AllRequirements

  object autoImport {
    val compileSass = TaskKey[Seq[File]]("compileSass", "Create css files from scss and sass files.")
  }

  import autoImport._

  override def projectSettings = Seq(
    // exclude sass files from being copied into the target dir by SbtWeb
    Assets / excludeFilter := HiddenFileFilter || "*.sass" || "*.scss",
    // define what sass files need to be compiled
    Assets / compileSass / excludeFilter := HiddenFileFilter || "_*",
    Assets / compileSass / includeFilter := "*.sass" || "*.scss",
    // tell SbtWeb that there's some stuff to copy into public
    Assets / managedResourceDirectories += (Assets / compileSass / resourceManaged).value,
    // define where to compile the sass into so that it can then be copied into public by SbtWeb
    Assets / compileSass / resourceManaged := webTarget.value / "sass" / "main",
    // make sure that we compile sass when assets are compiled by SbtWeb
    Assets / resourceGenerators += Assets / compileSass,
    // define how sass files should be compiled
    Assets / compileSass := Def
      .task {
        val sourceDir  = (Assets / sourceDirectory).value
        val sourcePath = sourceDir.toPath
        val targetPath = (Assets / compileSass / resourceManaged).value.toPath

        // Assets / webModules unpacks webjars to the webJarsDirectory target/web-modules/main
        val sassLoadPaths = List[java.io.File](
          (Assets / webJarsDirectory).value
        ).asJava

        // all files with sass extensions, excluding partials
        val sassFilesFilter = (
          (Assets / compileSass / includeFilter).value
            -- (Assets / compileSass / excludeFilter).value
        )

        // not actually sure why but it includes the directories in the list so we need to omit them
        val sassFilesFound = sourceDir.globRecursive(sassFilesFilter).get.filterNot(_.isDirectory)

        Using(SassCompilerFactory.bundled()) { sassCompiler =>
          sassCompiler.setLoadPaths(sassLoadPaths) // no need to set a path of the current file as well
          sassFilesFound.map { sassFile =>
            val cssFile = targetPath
              .resolve(sourcePath.relativize(sassFile.toPath))
              .resolveSibling(sassFile.base + ".css")
            Files.createDirectories(cssFile.getParent)
            Files.write(cssFile, sassCompiler.compileFile(sassFile).getCss.getBytes)
            cssFile.toFile
          }
        }.get // should probably have some better error reporting
      }
      .dependsOn(Assets / webModules)
      .value
  )
}
