pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity") version "3.19.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include("mockito-core")
// BOM project
include("mockito-bom")
// extensions
include(
    "mockito-extensions:mockito-android",
    "mockito-extensions:mockito-errorprone",
    "mockito-extensions:mockito-junit-jupiter",
    "mockito-extensions:mockito-proxy",
    "mockito-extensions:mockito-subclass",
)
// integration tests
include(
    "mockito-integration-tests:inline-mocks-tests",
    "mockito-integration-tests:extensions-tests",
    "mockito-integration-tests:groovy-tests",
    "mockito-integration-tests:groovy-inline-tests",
    "mockito-integration-tests:kotlin-tests",
    "mockito-integration-tests:kotlin-release-coroutines-tests",
    "mockito-integration-tests:junit-jupiter-extension-tests",
    "mockito-integration-tests:junit-jupiter-inline-mock-maker-extension-tests",
    "mockito-integration-tests:module-tests",
    "mockito-integration-tests:module-named-tests",
    "mockito-integration-tests:memory-tests",
    "mockito-integration-tests:junit-jupiter-parallel-tests",
    "mockito-integration-tests:osgi-tests",
    "mockito-integration-tests:programmatic-tests",
    "mockito-integration-tests:java-21-tests",
)

// https://developer.android.com/studio/command-line/variables#envar
// https://developer.android.com/studio/build#properties-files
if (providers.environmentVariable("ANDROID_HOME").isPresent || File("local.properties").exists()) {
    include("mockito-integration-tests:android-tests")
} else {
    logger.info("Not including android test project due to missing SDK configuration")
}

rootProject.name = "mockito"

//Posting Build scans to https://scans.gradle.com
develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
    }
}

buildCache {
    local {
        isEnabled = !providers.environmentVariable("CI").isPresent
    }
}
