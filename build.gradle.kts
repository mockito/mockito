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
    id("com.github.ben-manes.versions") version "0.53.0"
    id("mockito.root.releasing-conventions")
    id("mockito.coverage-aggregation-conventions")

    // Top-level android plugin declarations required for android modules to work.
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
}

