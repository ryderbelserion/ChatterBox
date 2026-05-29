plugins {
    id("com.ryderbelserion.feather.core")

    `java-library`
}

val libs: VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.codemc.io/repository/maven-public/")

    maven("https://repo.crazycrew.us/snapshots/")
    maven("https://repo.crazycrew.us/libraries/")
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
            "group" to project.group.toString(),
            "artifact" to rootProject.name,
            "version" to rootProject.version,
            "description" to rootProject.description.toString(),

            // fabric
            "id" to rootProject.name.lowercase(),
            "fabricloader" to libs.findVersion("fabric-loader").get(),

            // generic
            "minecraft" to libs.findVersion("minecraft").get(),
        )

        with(copySpec {
            include("*manifest.json", "*paper-plugin.yml", "*fabric.mod.json")
            from("src/main/resources") {
                expand(inputs.properties)
            }
        })
    }
}