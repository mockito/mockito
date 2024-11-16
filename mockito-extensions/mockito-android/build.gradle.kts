plugins {
    id("mockito.java-library-conventions")
    id("mockito.publication-conventions")
}

description = "Mockito for Android"

dependencies {
    api(project(":mockito-core"))
    implementation(libs.bytebuddy.android)
}

tasks.javadoc {
    isEnabled = false
}
