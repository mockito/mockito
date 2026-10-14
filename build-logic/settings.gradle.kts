dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include("conventions")

// Plugin builds do not inherit the main build's cache configuration.
buildCache {
    local {
        isEnabled = !providers.environmentVariable("CI").isPresent
    }
}
