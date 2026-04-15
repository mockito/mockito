// Module-level android configuration.
// https://developer.android.com/build/migrate-to-catalogs

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("mockito.test-conventions")
}

android {
    namespace = "org.mockitousage.androidtest"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    testCoverage {
        jacocoVersion = libs.versions.jacoco.get()
    }
}

androidComponents {
    beforeVariants(selector().withBuildType("release")) {
        it.enable = false
    }
}

// Exclude mockito-android from JVM tests, because otherwise it clashes with :mockito-core.
configurations.testImplementation {
    exclude (group = "org.mockito", module = "mockito-android")
}

dependencies {
    implementation(libs.kotlin.stdlib)

    runtimeOnly(project(":mockito-extensions:mockito-android"))

    testImplementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)

    androidTestImplementation(libs.android.runner)
    androidTestImplementation(libs.android.junit)
    androidTestImplementation(project(":mockito-extensions:mockito-android"))
}

// Coverage report that includes mockito-core classes (not just this module's classes).
// AGP 8+ finalizes JacocoReportTask.classFileCollection, so we use a standard Gradle
// JacocoReport task instead of modifying the AGP-internal task.
tasks.register<JacocoReport>("createMockitoCoverageReport") {
    dependsOn("createDebugAndroidTestCoverageReport")
    group = "verification"
    description = "Generates a Jacoco coverage report including mockito-core classes."

    // Use coverage data produced by connected tests
    executionData.setFrom(
        fileTree(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected")) {
            include("**/*.ec")
        }
    )

    // Include compiled classes from all Java subprojects (mockito-core, etc.)
    classDirectories.setFrom(
        rootProject.allprojects.filter { it.plugins.hasPlugin("java") }.map { proj ->
            proj.sourceSets["main"].output.asFileTree.matching {
                exclude("**/internal/util/concurrent/**")
            }
        }
    )

    sourceDirectories.setFrom(
        rootProject.allprojects.filter { it.plugins.hasPlugin("java") }.flatMap { proj ->
            proj.sourceSets["main"].allJava.srcDirs
        }
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
