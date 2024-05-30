import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id(libs.plugins.ksp.get().pluginId)
    kotlin(libs.plugins.serialization.get().pluginId).version(libs.versions.kotlin)
}

android {
    namespace = "com.elroid.currency.data.remote"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()

        val cfApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("CURRENCY_FREAKS_API_KEY")
        buildConfigField("String", "CF_API_KEY", "\"$cfApiKey\"")
    }
    compileOptions {
        val javaVersion = JavaVersion.toVersion(libs.versions.java.get().toInt())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    buildFeatures.buildConfig = true
}

dependencies {

    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.networking.okhttp)
    implementation(libs.networking.okhttp.logging)
    implementation(libs.networking.retrofit)
    implementation(libs.networking.retrofit.serialization)

    // Global Libs
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.utilities.kermit)

    // Android Standard
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockk.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}