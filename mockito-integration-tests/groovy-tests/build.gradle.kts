plugins {
    id("groovy")
    id("mockito.test-conventions")
}

description = "Integration test for using Mockito from Groovy."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(libs.groovy)
    testImplementation(libs.junit4)
}
