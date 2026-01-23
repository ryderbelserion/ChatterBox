plugins {
    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.kyori)
    compileOnly(libs.fusion.kyori)

    api(project(":chatterbox-api"))

    compileOnly(libs.luckperms)
}