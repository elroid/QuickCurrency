plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.elroid.currency.core.common"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }
    compileOptions {
        val javaVersion = JavaVersion.toVersion(libs.versions.java.get().toInt())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
}

dependencies {
    // Global Libs
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.utilities.kermit)
}