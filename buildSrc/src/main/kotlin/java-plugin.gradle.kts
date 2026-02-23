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
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        inputs.properties(
            "group" to project.group.toString(),
            "artifact" to rootProject.name,
            "version" to project.version,
            "description" to rootProject.description.toString(),
            "minecraft" to libs.findVersion("minecraft").get(),
        )

        with(copySpec {
            include("*manifest.json", "*paper-plugin.yml")
            from("src/main/resources") {
                expand(inputs.properties)
            }
        })
    }
}