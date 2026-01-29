plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"
project.version = "1.0.0"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.kyori)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}