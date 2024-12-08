import java.time.OffsetDateTime
import java.time.ZoneOffset

plugins {
    id("com.github.hierynomus.license")
}

val licenseHeaderFile = rootProject.file("doc/licenses/HEADER.txt")

license {
    header = licenseHeaderFile
    strictCheck = true
    ignoreFailures = true
    skipExistingHeaders = true
    mapping(mapOf(
        "java" to "SLASHSTAR_STYLE",
        "groovy" to "SLASHSTAR_STYLE",
        "kt" to "SLASHSTAR_STYLE",
    ))

    exclude("junit-platform.properties")
    exclude("mockito-extensions/org.mockito.plugins.*")
    exclude("META-INF/services/org.junit.jupiter.api.extension.Extension")
    exclude("org/mockito/internal/util/concurrent/README.md")
    exclude("org/mockito/internal/util/concurrent/LICENSE")


    ext["year"] = OffsetDateTime.now(ZoneOffset.UTC).year
}
