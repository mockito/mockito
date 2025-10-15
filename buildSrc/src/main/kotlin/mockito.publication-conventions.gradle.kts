import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.withType

plugins {
    `maven-publish`
    signing
}

// enforce the same group for all published artifacts
group = "org.mockito"

val localRepoForTesting = rootProject.layout.buildDirectory.dir("repo")

// generic publication conventions
publishing {
    publications {
        withType(MavenPublication::class).configureEach {
            pom {
                artifactId = project.base.archivesName.get()
                name = artifactId
                description = providers.provider { project.description }
                url = "https://github.com/mockito/mockito"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                        distribution = "repo"
                    }
                }
                developers {
                    listOf(
                        "mockitoguy:Szczepan Faber",
                        "bric3:Brice Dutheil",
                        "raphw:Rafael Winterhalter",
                        "TimvdLippe:Tim van der Lippe"
                    ).forEach { devData ->
                        developer {
                            val devInfo = devData.split(":")
                            id = devInfo[0]
                            name = devInfo[1]
                            url = "https://github.com/" + devInfo[0]
                            roles = listOf("Core developer")
                        }
                    }
                }
                scm {
                    url = "https://github.com/mockito/mockito.git"
                }
                issueManagement {
                    url = "https://github.com/mockito/mockito/issues"
                    system = "GitHub issues"
                }
                ciManagement {
                    url = "https://github.com/mockito/mockito/actions"
                    system = "GH Actions"
                }
            }
        }
    }

    //useful for testing - running "publish" will create artifacts/pom in a local dir
    repositories {
        maven(uri(localRepoForTesting)) {
            name = "local"
        }
    }
}

tasks.clean {
    doLast {
        delete(localRepoForTesting)
    }
}

// Java platform publication conventions (BOM file)
plugins.withType(JavaPlatformPlugin::class) {
    publishing {
        publications {
            register<MavenPublication>("mockitoPlatform") {
                from(components["javaPlatform"])
            }
        }
    }
}

// Java library publication conventions
plugins.withType<JavaLibraryPlugin>().configureEach {
    // Sources/javadoc artifacts required by Maven module publications
    val licenseSpec = copySpec {
        from(project.rootDir)
        include("LICENSE")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(project.sourceSets["main"].allSource)
        with(licenseSpec)
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.named("javadoc"))
        with(licenseSpec)
    }

    project.artifacts {
        archives(sourcesJar)
        archives(javadocJar)
    }

    tasks.named("jar", Jar::class) {
        with(licenseSpec)
    }

    // only create a 'java' publication when the JavaPlugin is present
    publishing {
        publications {
            register<MavenPublication>("mockitoLibrary") {
                from(components["java"])
                artifact(sourcesJar)
                artifact(javadocJar)

                pom {
                    // Gradle does not write 'jar' packaging to the pom (unlike other packaging types).
                    // This is OK because 'jar' is implicit/default:
                    // https://maven.apache.org/guides/introduction/introduction-to-the-pom.html#minimal-pom
                    packaging = project.tasks.named<Jar>("jar").get().archiveExtension.get()
                }
            }
        }
    }

    tasks.named<GenerateMavenPom>("generatePomFileForMockitoLibraryPublication") {
        doLast {
            // validates that the pom has correct artifact id to avoid issues like #1444
            destination.readText().let { pomContent ->
                require(pomContent.contains("<artifactId>${project.base.archivesName.get()}</artifactId>")) {
                    "POM file does not contain the correct artifactId: ${project.base.archivesName.get()}"
                }
                require(pomContent.contains("<name>${project.base.archivesName.get()}</name>")) {
                    "POM file does not contain the correct name: ${project.base.archivesName.get()}"
                }
            }
        }
    }

    // fleshes out problems with Maven pom generation when building
    tasks.build {
        dependsOn("publishAllPublicationsToLocalRepository")
    }
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}

// https://docs.gradle.org/current/userguide/signing_plugin.html
signing {
    if (providers.environmentVariable("PGP_KEY").isPresent) {
        useInMemoryPgpKeys(
            providers.environmentVariable("PGP_KEY").orNull,
            providers.environmentVariable("PGP_PWD").orNull
        )
        sign(publishing.publications)
    }
}
