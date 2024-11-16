plugins {
    id("mockito.library-conventions")
}

description = "Mockito preconfigured proxy mock mock maker (to support interfaces without code generation)"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}

tasks.javadoc {
    isEnabled = false
}
