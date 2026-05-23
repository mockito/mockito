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

    // Top-level android plugin declarations required for android modules to work.
    // Use id() instead of alias() because AGP is on the buildSrc classpath.
    id("com.android.application") apply false
    id("com.android.library") apply false
}


