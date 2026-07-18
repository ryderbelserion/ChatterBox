plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.velocity)
    implementation(libs.bstats.velocity)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    build {
        dependsOn(shadowJar)
    }
}