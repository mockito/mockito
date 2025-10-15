plugins {
    id("groovy")
    id("mockito.test-conventions")
}


description = "Integration test for using mockito-inline with Groovy."

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(libs.groovy)
    testImplementation(libs.junit4)
}
