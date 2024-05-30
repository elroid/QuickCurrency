plugins {
    id(libs.plugins.jetbrainsKotlinJvm.get().pluginId)
    id(libs.plugins.ksp.get().pluginId)
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
    implementation(projects.core.model)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.utilities.kermit)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.mockk)
}