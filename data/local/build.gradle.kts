plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id(libs.plugins.ksp.get().pluginId)
    kotlin(libs.plugins.serialization.get().pluginId).version(libs.versions.kotlin)
    alias(libs.plugins.roomPlugin)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.elroid.currency.data.local"
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
    ksp {
        arg("KOIN_DEFAULT_MODULE","false")
    }
}

dependencies {

    implementation(projects.core.common)
    implementation(projects.core.model)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)

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
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}