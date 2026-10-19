plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.junit-jupiter-compatibility-conventions")
}

description = "End-to-end tests for Mockito and its extensions."

junitCompatibility {
    runtimes {
        register("junit5") {
            bom = libs.junit5.bom
            testJvmVersion = 11
        }
        register("junit6") {
            bom = libs.junit6.bom
            testJvmVersion = 17
        }
    }
}

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(testFixtures(project(":mockito-core")))
    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    add("osgiTestBundleImplementation", project(":mockito-extensions:mockito-junit-jupiter"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}
