plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.elroid.currency"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.elroid.currency"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        val javaVersion = JavaVersion.toVersion(libs.versions.java.get().toInt())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("KOIN_DEFAULT_MODULE", "false")
    }
}

dependencies {

    //import all modules
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.data.repository)
    implementation(projects.data.local)
    implementation(projects.data.remote)
    implementation(projects.feature.common)
    implementation(projects.feature.converter)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    ksp(libs.koin.ksp.compiler)
    implementation(libs.kotlinx.coroutinesCore)

    implementation(libs.utilities.kermit)

    testImplementation(libs.koin.android.test)
}