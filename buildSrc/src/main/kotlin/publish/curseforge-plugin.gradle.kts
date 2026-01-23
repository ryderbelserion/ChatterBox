@file:Suppress("PackageDirectoryMismatch")

import gradle.kotlin.dsl.accessors._ba5d290ab715e74c9896b4fa811d61b7.ext
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    id("net.darkhax.curseforgegradle")

    id("shared-plugin")
}

val libs: VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

tasks.register("publishCurseForge", net.darkhax.curseforgegradle.TaskPublishCurseForge::class) {
    apiToken = System.getenv("CURSEFORGE_TOKEN")

    val projectId = rootProject.property("cf_project_id") as String?

    var mainFile = upload(projectId, tasks.named<Jar>("jar").flatMap { it.archiveFile })

    mainFile.addGameVersion(rootProject.property("cf_game_version") as String?)
    mainFile.releaseType = rootProject.ext.get("release_type").toString().uppercaseFirstChar()
    mainFile.displayName = "${rootProject.version as String}-${project.name}"
    mainFile.changelog = rootProject.file("changelog.md").readText()

}