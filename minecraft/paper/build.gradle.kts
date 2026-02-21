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

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        listOf(
            "com.ryderbelserion.fusion"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}