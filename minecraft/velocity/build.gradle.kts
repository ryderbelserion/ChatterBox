plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.mojang)
    implementation(libs.fusion.kyori)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    build {
        dependsOn(shadowJar)
    }
}