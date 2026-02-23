plugins {
    `paper-plugin`
}

project.group = "${rootProject.group}.paper"

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.paper)
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
        archiveBaseName.set("${rootProject.name}-Paper")
        archiveClassifier.set("")

        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))

        from(rootProject.layout.projectDirectory.dir("configs").dir("minecraft")) {
            into("/")
        }

        listOf(
            "com.ryderbelserion.fusion"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}