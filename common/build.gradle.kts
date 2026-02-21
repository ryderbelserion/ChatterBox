plugins {
    `java-plugin`
}

dependencies {
    api(project(":chatterbox-discord"))
    api(project(":chatterbox-api"))
    api(libs.jda)

    compileOnly(libs.bundles.kyori)
    compileOnly(libs.fusion.kyori)
    compileOnly(libs.luckperms)
    compileOnly(libs.log4j2)
}