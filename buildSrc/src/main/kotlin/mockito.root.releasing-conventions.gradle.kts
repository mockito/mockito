plugins {
    id("org.shipkit.shipkit-auto-version")
    id("org.shipkit.shipkit-changelog")
    id("org.shipkit.shipkit-github-release")

    id("io.github.gradle-nexus.publish-plugin")
}

val isSnapshot = version.toString().endsWith("SNAPSHOT")
tasks {
    generateChangelog {
        githubToken = providers.environmentVariable("GITHUB_TOKEN").get()
        previousRevision = project.ext["shipkit-auto-version.previous-tag"].toString()
        repository = "mockito/mockito"
    }

    githubRelease {
        enabled = !isSnapshot
        dependsOn(generateChangelog)
        githubToken = providers.environmentVariable("GITHUB_TOKEN").get()
        newTagRevision = providers.environmentVariable("GITHUB_SHA").get()
        repository = generateChangelog.get().repository
        changelog = generateChangelog.get().outputFile
    }

    closeAndReleaseStagingRepository {
        enabled = !isSnapshot
    }

    val releaseSummary by registering {
        doLast {
            if (isSnapshot) {
                println(
                    """
                    RELEASE SUMMARY
                      SNAPSHOTS released to: https://s01.oss.sonatype.org/content/repositories/snapshots/org/mockito/mockito-core
                      Release to Maven Central: SKIPPED FOR SNAPSHOTS
                      Github releases: SKIPPED FOR SNAPSHOTS
                    """.trimIndent()
                )
            } else {
                println(
                    """
                    RELEASE SUMMARY
                      Release to Maven Central (available in few hours): https://repo1.maven.org/maven2/org/mockito/mockito-core/
                      Github releases: https://github.com/mockito/mockito/releases
                    """.trimIndent()
                )
            }
        }
    }
}

nexusPublishing {
    packageGroup = "org.mockito"
    repositoryDescription = provider { "Mockito ${project.version}" }

    repositories {
        if (!providers.environmentVariable("NEXUS_TOKEN_PWD").orNull.isNullOrBlank()) {
            sonatype {
                // Publishing to: https://s01.oss.sonatype.org (faster instance)
                nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
                snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

                username = providers.environmentVariable("NEXUS_TOKEN_USER")
                password = providers.environmentVariable("NEXUS_TOKEN_PWD")
            }
        }
    }
}

