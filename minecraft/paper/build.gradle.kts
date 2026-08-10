plugins {
    `paper-plugin`
}

project.group = "${rootProject.group}.paper"

repositories {
    maven("https://repo.fancyinnovations.com/releases/")

    maven("https://repo.momirealms.net/releases/")

    maven("https://repo.hibiscusmc.com/releases/")

    maven("https://repo.nexomc.com/releases/")

    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    implementation(project(":chatterbox-common"))

    implementation(libs.fusion.paper)

    implementation(libs.bstats.paper)

    compileOnly(libs.bundles.shared)
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
}