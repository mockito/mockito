apply(from = "$rootDir/gradle/dependencies.gradle")

plugins {
    java
}

description = "End-to-end tests for automatic registration of MockitoExtension with the inline mock maker."

dependencies {
    testCompile(project(":junit-jupiter"))
    testCompile(library("assertj"))
    testCompile(library("junitPlatformLauncher"))
    testCompile(library("junitJupiterApi"))
    testRuntime(library("junitJupiterEngine"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val Project.libraries
    get() = rootProject.extra["libraries"] as Map<String, Any>

fun Project.library(name: String) =
    libraries[name]!!
