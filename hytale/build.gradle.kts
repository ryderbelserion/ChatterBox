plugins {
    `shadow-plugin`
}

project.version = rootProject.version

dependencies {
    compileOnly(files(System.getenv("HYTALE_SERVER")))

    implementation(project(":chatterbox-common"))

    implementation(libs.bundles.kyori)
    implementation(libs.fusion.kyori)

    compileOnly(libs.luckperms)
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-Hytale-${rootProject.version}")
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