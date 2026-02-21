plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set("")

        exclude("META-INF/**")
    }
}