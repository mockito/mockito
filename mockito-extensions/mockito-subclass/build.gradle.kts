plugins {
    id("mockito.library-conventions")
}

description = "Mockito preconfigured subclass mock maker"

dependencies {
    api(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}

tasks.javadoc {
    isEnabled = false
}
