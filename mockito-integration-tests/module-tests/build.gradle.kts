plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Test suite for Java 9 modules with Mockito"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}
