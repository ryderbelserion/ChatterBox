plugins {
    `shadow-plugin`
}

project.group = "${rootProject.group}.hytale"

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.bundles.kyori)
    implementation(libs.fusion.kyori)
    implementation(libs.log4j2)

    compileOnly(libs.luckperms)
    compileOnly(libs.hytale)
}

tasks {
    shadowJar {
        archiveBaseName.set("${rootProject.name}-Hytale")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        from(rootProject.layout.projectDirectory.dir("configs").dir("minecraft")) {
            into("/")
        }

        listOf(
            "com.ryderbelserion.fusion",
            "net.kyori.adventure"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}