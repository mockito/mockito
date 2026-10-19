plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.junit-jupiter-compatibility-conventions")
}

description = "End-to-end tests for automatic registration and OSGi support of MockitoExtension."

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
    testImplementation(project(":mockito-extensions:mockito-junit-jupiter"))
    testImplementation(libs.assertj)
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.bundles.junit5.runtime)

    add("osgiTestBundleImplementation", project(":mockito-extensions:mockito-junit-jupiter"))
}

tasks {
    test {
        useJUnitPlatform()
    }
}
