plugins {
    `shadow-plugin`
}

repositories {

}

dependencies {
    implementation("com.ryderbelserion.fusion", "fusion-files", "3.4.3")

    compileOnly(files(System.getenv("HYTALE_SERVER")))
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-${rootProject.version}")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
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