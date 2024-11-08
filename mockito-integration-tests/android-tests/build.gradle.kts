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
    afterEvaluate {
        // Enable coverage of mockito classes which are not part of this module.
        // Without these added classFileCollection dependencies the coverage data/report is empty.
        tasks.named<JacocoReportTask>("createDebugAndroidTestCoverageReport") jacocoTask@{
            // Clear the production code of this module: src/main/java, it has no mockito-user-visible code.
            classFileCollection.setFrom()
            rootProject.allprojects currentProject@{
                plugins.withId("java") {
                    val sourceSets = this@currentProject.sourceSets
                    // Mimic org.gradle.testing.jacoco.tasks.JacocoReportBase.sourceSets().
                    // Not possible to set task.javaSources because it's initialized with setDisallowChanges.
                    //task.javaSources.add({ [ sourceSets.main.allJava.srcDirs ] })
                    this@jacocoTask.classFileCollection.from(
                        sourceSets.named("main").get().output.asFileTree.matching {
                            exclude("**/internal/util/concurrent/**")
                        }
                    )
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
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
