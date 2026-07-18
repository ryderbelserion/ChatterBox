plugins {
    id("com.ryderbelserion.feather.core")

    `java-library`
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.codemc.io/repository/maven-public/")

    maven("https://repo.opencollab.dev/maven-snapshots/")

    maven("https://repo.triumphteam.dev/snapshots/")

    maven("https://repo.crazycrew.us/snapshots/")
    maven("https://repo.crazycrew.us/releases/")

    maven("https://jitpack.io/")

    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(25)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        inputs.properties(
            // global
            "artifact" to rootProject.name,
            "group" to project.group,
            "version" to rootProject.version,
            "description" to rootProject.description.toString(),

            // fabric
            "id" to rootProject.name.lowercase(),
            "fabricloader" to libs.findVersion("fabric-loader").get(),

            // generic
            "minecraft" to libs.findVersion("minecraft").get(),

            "website" to "https://github.com/${rootProject.property("repository_owner")}/${rootProject.name}",

            "current_commit" to rootProject.ext.get("current_commit").toString(),
            "previous_commit" to rootProject.ext.get("previous_commit").toString()
        )

        with(copySpec {
            include("*paper-plugin.yml", "*version.json")

            from("src/main/resources") {
                expand(inputs.properties)
            }
        })
    }
}