plugins {
    `shadow-plugin`
}

repositories {

}

dependencies {
    compileOnly(files(System.getenv("HYTALE_SERVER")))

    implementation(project(":chatterbox-common"))

    implementation(libs.bundles.kyori)
    implementation(libs.fusion.files)

    compileOnly(libs.luckperms)
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-${rootProject.version}")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        relocate("net.kyori.adventure", "com.ryderbelserion.chatterbox.libs.adventure")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        inputs.properties(
            "group" to rootProject.group.toString(),
            "artifact" to rootProject.name,
            "version" to rootProject.version,
            "description" to rootProject.description.toString(),
        )

        with(copySpec {
            include("*manifest.json")
            from("src/main/resources") {
                expand(inputs.properties)
            }
        })
    }
}