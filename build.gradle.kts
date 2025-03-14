// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.kotlinAndroidKsp) apply false
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(libs.secrets.gradle.plugin)
        classpath(libs.objectbox.gradle.plugin)
    }
}