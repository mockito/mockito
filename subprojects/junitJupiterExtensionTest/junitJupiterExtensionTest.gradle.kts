apply(from = "$rootDir/gradle/dependencies.gradle")

plugins {
    java
}

description = "End-to-end tests for automatic registration of MockitoExtension."

dependencies {
    testImplementation(project(":junit-jupiter"))
    testImplementation(library("assertj"))
    testImplementation(library("junitJupiterApi"))
    testRuntimeOnly(library("junitJupiterEngine"))
    testRuntimeOnly(library("junitPlatformLauncher"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val Project.libraries
    get() = @Suppress("UNCHECKED_CAST") (rootProject.extra["libraries"] as Map<String, Any>)

fun Project.library(name: String) =
    libraries[name]!!
