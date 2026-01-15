plugins {
    `java-plugin`
}

repositories {

}

dependencies {
    implementation("com.ryderbelserion.fusion", "fusion-files", "3.4.3")

    compileOnly(files(System.getenv("HYTALE_SERVER")))
}