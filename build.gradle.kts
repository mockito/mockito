buildscript {
    repositories {
        mavenLocal() //for local testing of mockito-release-tools
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("gradle.plugin.com.hierynomus.gradle.plugins:license-gradle-plugin:0.16.1")

        classpath("io.github.gradle-nexus:publish-plugin:2.0.0-rc-1")
        classpath("org.shipkit:shipkit-changelog:2.0.1")
        classpath("org.shipkit:shipkit-auto-version:2.1.0")

        classpath("com.google.googlejavaformat:google-java-format:1.24.0")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    }
}

plugins {
    id("eclipse")
    id("com.diffplug.spotless") version "6.25.0"
    id("com.github.ben-manes.versions") version "0.51.0"
}

apply {
    from("$rootDir/gradle/shipkit.gradle")
    from("$rootDir/gradle/license.gradle")
}

subprojects {
    plugins.withId("java") {
        apply(from = "$rootDir/gradle/spotless.gradle")
    }
}
