plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.velocity) {
        exclude(module = "configurate-yaml")
        exclude(module = "configurate-gson")
    }

    implementation(libs.bstats.velocity)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")

        listOf(
            "com.ryderbelserion.fusion"
        ).forEach {
            relocate(it, "libs.$it")
        }

        relocate("org.bstats", project.group.toString())

        minimize {
            exclude(dependency("com.ryderbelserion.fusion:.*"))
        }
    }
}