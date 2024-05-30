plugins {
    id(libs.plugins.jetbrainsKotlinJvm.get().pluginId)
}

val javaVersionNumber = libs.versions.java.get().toInt()
java {
    val javaVersion = JavaVersion.toVersion(javaVersionNumber)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

kotlin {
    jvmToolchain(javaVersionNumber)
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.utilities.kermit)
}