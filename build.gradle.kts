buildscript {
    repositories {
        mavenLocal() //for local testing of mockito-release-tools
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("${libs.plugins.kotlin.get()}")
    }
}

plugins {
    id("eclipse")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("mockito.root.releasing-conventions")

    // Top-level android plugin declaration required for :mockito-integration-tests:android-tests to work
    alias(libs.plugins.android.application) apply false
}


