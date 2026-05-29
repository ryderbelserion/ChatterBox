plugins {
    `shadow-plugin`
}

project.group = "${rootProject.group}.hytale"
project.version = "1.3.0"

repositories {
    maven("https://maven.hytale.com/release/")
}

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.bundles.kyori)
    implementation(libs.bstats.hytale)
    implementation(libs.fusion.kyori)
    implementation(libs.log4j2)

    compileOnly(libs.luckperms)
    compileOnly(libs.hytale)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set("${rootProject.name}-Hytale")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        listOf(
            "com.ryderbelserion.fusion",
            "net.kyori.adventure"
        ).forEach {
            relocate(it, "libs.$it")
        }

        relocate("org.bstats", project.group.toString())
    }
}