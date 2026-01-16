plugins {
    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.kyori)
    compileOnly(libs.fusion.files)

    api(project(":chatterbox-api"))
}