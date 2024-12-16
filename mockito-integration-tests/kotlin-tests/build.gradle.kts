import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("mockito.test-conventions")
}

description = "Kotlin tests for Mockito."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(libs.junit4)

    testImplementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.coroutines)
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
}

