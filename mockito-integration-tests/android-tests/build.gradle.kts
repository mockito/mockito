import com.android.build.gradle.internal.coverage.JacocoReportTask

// Module-level android configuration.
// https://developer.android.com/build/migrate-to-catalogs

plugins {
    alias(libs.plugins.android.application)
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

    kotlinOptions {
        jvmTarget = "11"
    }

    testCoverage {
        jacocoVersion = libs.versions.jacoco.get()
    }
}

tasks.named<JacocoReportTask>("createDebugAndroidTestCoverageReport") {
    getSourceFolders().from(File(project(":mockito-core").getProjectDir(), "src/main/java"))
    getSourceFolders().from(File(project(":mockito-extensions:mockito-android").getProjectDir(), "src/main/java"))
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

    // Add :android on the classpath so that AGP's jacoco setup thinks it's "production code to be tested".
    // Essentially a way to say: tasks.createDebugAndroidTestCoverageReport.classFileCollection.from(project(":android"))
    runtimeOnly(project(":mockito-extensions:mockito-android"))

    testImplementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)

    androidTestImplementation(libs.android.runner)
    androidTestImplementation(libs.android.junit)
    androidTestImplementation(project(":mockito-extensions:mockito-android"))
}
