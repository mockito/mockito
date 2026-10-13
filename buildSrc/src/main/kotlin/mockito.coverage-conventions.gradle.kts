import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    jacoco
}

val jacocoVersion = extensions.getByType<VersionCatalogsExtension>()
    .named("libs").findVersion("jacoco").get().requiredVersion

jacoco {
    toolVersion = jacocoVersion
}
