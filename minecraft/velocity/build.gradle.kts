plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"
project.version = "1.0.0"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.mojang)
    implementation(libs.fusion.kyori)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    shadowJar {
        archiveBaseName.set("${rootProject.name}-Velocity")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        listOf(
            "com.ryderbelserion.fusion"
        ).forEach {
            relocate(it, "com.ryderbelserion.libs.$it")
        }
    }
}