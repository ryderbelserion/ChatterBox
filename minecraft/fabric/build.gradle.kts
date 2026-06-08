plugins {
    alias(libs.plugins.fabric.loom)

    id("java-plugin")
}

project.group = "${rootProject.group}.fabric"

repositories {
    maven("https://maven.fabricmc.net/")
}

dependencies {
    compileOnly(libs.fabric.loader)
    compileOnly(libs.fabric.api)

    minecraft(libs.minecraft)

    implementation("net.kyori:adventure-text-serializer-gson:5.1.1")
    include("net.kyori:adventure-text-serializer-gson:5.1.1")

    implementation(libs.bundles.kyori)
    include(libs.bundles.kyori)
}