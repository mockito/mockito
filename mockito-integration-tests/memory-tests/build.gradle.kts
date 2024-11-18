plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Test suite memory usage of Mockito"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}

tasks {
    test {
        maxHeapSize = "128m"
    }
}
