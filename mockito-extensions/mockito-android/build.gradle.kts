plugins {
    id("mockito.java-library-conventions")
    id("mockito.publication-conventions")
}

description = "Mockito for Android"

dependencies {
    api(project(":mockito-core"))
    implementation(libs.bytebuddy.android)
    // Test dependencies
    testImplementation("junit:junit:4.13.2") // JUnit for testing
    testImplementation("org.mockito:mockito-core:5.15.2") // Mockito core, match Mockito version
    testImplementation("org.powermock:powermock-api-mockito2:2.0.9") // PowerMock for static mocking
    testImplementation("org.powermock:powermock-module-junit4:2.0.9")
}

tasks.javadoc {
    isEnabled = false
}

tasks.withType<Test> {
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
