plugins {
    `java-plugin`
}

project.group = "${rootProject.group}.discord"

dependencies {
    compileOnly(libs.fusion.core)
    compileOnly(libs.classic)

    api(libs.jda)
}