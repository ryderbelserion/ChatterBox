plugins {
    `shadow-plugin`
}

project.group = "${rootProject.group}.hytale"
project.version = rootProject.version

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    implementation(project(":chatterbox-common"))
    implementation(libs.bundles.kyori)
    implementation(libs.fusion.kyori)

    compileOnly(libs.luckperms)
    compileOnly(libs.hytale)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        listOf(
            "com.ryderbelserion.fusion",
            "net.kyori.adventure"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}