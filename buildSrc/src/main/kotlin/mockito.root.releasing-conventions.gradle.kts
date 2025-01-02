plugins {
    id("org.shipkit.shipkit-auto-version")
    id("org.shipkit.shipkit-changelog")
    id("org.shipkit.shipkit-github-release")

    id("io.github.gradle-nexus.publish-plugin")
}

val isSnapshot = version.toString().endsWith("SNAPSHOT")
val githubTokenProvider = providers.environmentVariable("GITHUB_TOKEN").orElse("")
val githubShaProvider = providers.environmentVariable("GITHUB_SHA").orElse("")
val mockitoRepository = "mockito/mockito"

tasks {
    generateChangelog {
        githubToken = githubTokenProvider.get()
        previousRevision = project.ext["shipkit-auto-version.previous-tag"].toString()
        repository = mockitoRepository
    }

    githubRelease {
        enabled = !isSnapshot
        dependsOn(generateChangelog)
        githubToken = githubTokenProvider.get()
        newTagRevision = githubShaProvider.get()
        repository = mockitoRepository
        changelog = generateChangelog.get().outputFile
    }

    closeAndReleaseStagingRepositories {
        enabled = !isSnapshot
    }

    val releaseSummary by registering {
        doLast {
            if (isSnapshot) {
                logger.lifecycle(
                    """
                    RELEASE SUMMARY
                      SNAPSHOTS released to: https://s01.oss.sonatype.org/content/repositories/snapshots/org/mockito/mockito-core
                      Release to Maven Central: SKIPPED FOR SNAPSHOTS
                      Github releases: SKIPPED FOR SNAPSHOTS
                    """.trimIndent()
                )
            } else {
                logger.lifecycle(
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

