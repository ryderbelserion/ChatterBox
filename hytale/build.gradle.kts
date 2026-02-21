plugins {
    `shadow-plugin`
}

project.group = "${rootProject.group}.hytale"

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    implementation(project(":chatterbox-common"))
    implementation(libs.bundles.kyori)
    implementation(libs.fusion.kyori)
    implementation(libs.log4j2)

    compileOnly(libs.luckperms)
    compileOnly(libs.hytale)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}