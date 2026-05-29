plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        exclude("META-INF/**")

        listOf(
            "org.bstats"
        ).forEach {
            relocate(it, project.group.toString())
        }
    }
}