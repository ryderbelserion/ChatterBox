plugins {
    `shadow-plugin`
}

project.group = "${rootProject.group}.hytale"
project.version = rootProject.version

dependencies {
    compileOnly(files(System.getenv("HYTALE_SERVER")))

    implementation(project(":chatterbox-common")) {
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "net.dv8tion", module = "JDA")
    }

    implementation(libs.bundles.kyori)
    implementation(libs.fusion.kyori)

    compileOnly(libs.luckperms)
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-Hytale")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        listOf(
            "com.ryderbelserion.fusion",
            "net.kyori.adventure"
        ).forEach {
            relocate(it, "com.ryderbelserion.libs.$it")
         }
    }
}