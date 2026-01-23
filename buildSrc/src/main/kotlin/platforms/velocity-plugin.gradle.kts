plugins {
    id("xyz.jpenilla.run-paper")

    id("shadow-plugin")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(libs.findLibrary("velocity").get())
}