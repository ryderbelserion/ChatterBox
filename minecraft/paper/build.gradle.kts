plugins {
    `paper-plugin`
}

project.group = "${rootProject.group}.paper"
project.version = "1.0.0"

dependencies {
    implementation(libs.bundles.cloud.paper)

    implementation(libs.fusion.paper)

    implementation(project(":chatterbox-common"))
}

tasks {
    runPaper.folia.registerTask()

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar {
        archiveBaseName.set("${rootProject.name}-Paper-${rootProject.version}")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        listOf(
            "com.ryderbelserion.fusion",
            "org.incendo"
        ).forEach {
            relocate(it, "com.ryderbelserion.libs.$it")
        }
    }
}