buildscript {
    repositories {
        mavenLocal() //for local testing of mockito-release-tools
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    }
}

plugins {
    id("eclipse")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("mockito.root.releasing-conventions")

    // Top-level android plugin declaration required for :mockito-integration-tests:android-tests to work
    alias(libs.plugins.android.application) apply false
}


