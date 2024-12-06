plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Test suite for exercising programmatic mock maker in Mockito"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}

tasks {
    test {
        forkEvery = 1
    }
}
