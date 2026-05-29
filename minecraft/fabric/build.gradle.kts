plugins {
    alias(libs.plugins.fabric.loom)

    id("java-plugin")
}

project.group = "${rootProject.group}.fabric"

repositories {
    maven("https://maven.fabricmc.net/")
}

dependencies {
    minecraft(libs.minecraft)

    compileOnly(libs.fabric.loader)
    compileOnly(libs.fabric.api)
}