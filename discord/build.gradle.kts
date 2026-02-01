plugins {
    `java-plugin`
}

project.group = "${rootProject.group}.discord"
project.version = "1.0.0"

dependencies {
    compileOnly(libs.fusion.core)
    compileOnly(libs.classic)

    api(libs.jda)
}