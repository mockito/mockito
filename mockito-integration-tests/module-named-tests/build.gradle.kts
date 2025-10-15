plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Test suite for Java 9 modules using a named declaration with Mockito"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}
