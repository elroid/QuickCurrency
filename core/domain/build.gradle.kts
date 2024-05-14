plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    val javaVersion = JavaVersion.toVersion(libs.versions.java.get().toInt())
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}