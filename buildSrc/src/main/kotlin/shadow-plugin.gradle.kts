plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        archiveClassifier.set("")

        exclude("META-INF/**")

        listOf(
            "com.ryderbelserion.fusion",
            "org.spongepowered",
            //"org.apache",
            //"org.slf4j",

            //"com.neovisionaries",
            //"com.fasterxml",
            "com.google",

            //"google.protobuf",
            "io.leangen",
            //"gnu.trove",

            //"org.jetbrains",
            "org.jspecify",
            //"org.intellij",

            //"net.dv8tion",
            "org.bstats",

            //"okhttp3",
            //"kotlin",
            //"javax",
            //"okio"
        ).forEach {
            relocate(it, "libs.$it")
        }

        minimize {
            exclude(dependency("com.ryderbelserion.fusion:.*"))
        }
    }
}