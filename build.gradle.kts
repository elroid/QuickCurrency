// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.ksp)
    kotlin(libs.plugins.serialization.get().pluginId).version(libs.versions.kotlin)
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.roomPlugin) apply false
}