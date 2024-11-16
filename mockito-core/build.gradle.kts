import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("mockito.library-conventions")
    id("mockito.java-backward-compatibility-checks-conventions")
    id("mockito.javadoc-conventions")
}

description = "Mockito mock objects library core API and implementation"
base.archivesName = "mockito-core"

apply {
    from(rootProject.file("gradle/coverage.gradle"))
}

val testUtil: Configuration by configurations.creating //TODO move to separate project
configurations {
    // Putting 'provided' dependencies on test compile and runtime classpath.
    testCompileOnly.configure {
        extendsFrom(compileOnly.get())
    }
    testRuntimeOnly.configure {
        extendsFrom(compileOnly.get())
    }
}

dependencies {
    api(libs.bytebuddy)
    api(libs.bytebuddy.agent)

    compileOnly(libs.junit4)
    compileOnly(libs.hamcrest)
    compileOnly(libs.opentest4j)
    implementation(libs.objenesis)

    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)

    testUtil(sourceSets.test.get().output)
}

mockitoJavadoc {
    title = "Mockito ${project.version} API"
    docTitle = """<h1><a href="org/mockito/Mockito.html">Click to see examples</a>. Mockito ${project.version} API.</h1>"""
}

val generatedInlineResource = layout.buildDirectory.dir("generated/resources/inline")
sourceSets {
    main {
        resources {
            output.dir(generatedInlineResource)
        }
    }
}

tasks {
    val copyMockMethodDispatcher by registering(Copy::class) {
        dependsOn(compileJava)

        from(sourceSets.main.flatMap { it.java.classesDirectory }.map { it.file("org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.class") })
        into(generatedInlineResource.map { it.dir("org/mockito/internal/creation/bytebuddy/inject") })

        rename("(.+)\\.class", "$1.raw")
    }
    classes.dependsOn(copyMockMethodDispatcher)


    jar {
        exclude(
            "org/mockito/internal/creation/bytebuddy/inject/package-info.class",
            "org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.class",
        )
        manifest {
            attributes(
                "Premain-Class" to "org.mockito.internal.PremainAttach",
                "Can-Retransform-Classes" to "true",
            )
        }

        bundle { // this: BundleTaskExtension
            classpath(project.configurations.compileClasspath)
            bnd("""
                # Bnd instructions for the Mockito core bundle.

                # Set a bundle name slightly different from project description.
                Bundle-Name: Mockito Mock Library for Java. Core bundle requires Byte Buddy and Objenesis.

                # Set the Bundle-SymbolicName to "org.mockito.mockito-core".
                # Otherwise bnd plugin will use archiveClassifier by default.
                Bundle-SymbolicName: org.mockito.mockito-core

                # Version rules for the bundle.
                # https://bnd.bndtools.org/macros/version_cleanup.html
                Bundle-Version: ${'$'}{version_cleanup;${project.version}}

                # Set bundle license
                Bundle-License: MIT

                # Export rules for public and internal packages
                # https://bnd.bndtools.org/heads/export_package.html
                Export-Package: \
                    org.mockito.internal.*;status=INTERNAL;mandatory:=status;version=${archiveVersion.get()}, \
                    org.mockito.*;version=${archiveVersion.get()}

                # General rules for package import
                # https://bnd.bndtools.org/heads/import_package.html
                # Override the default version policy to allow for a range of versions.
                # https://bnd.bndtools.org/instructions/versionpolicy.html
                -versionpolicy: [${'$'}{version;==;${'$'}{@}},${'$'}{version;+;${'$'}{@}})
                Import-Package: \
                    net.bytebuddy.*;version="[1.6.0,2.0)", \
                    junit.*;resolution:=optional, \
                    org.junit.*;resolution:=optional, \
                    org.hamcrest;resolution:=optional, \
                    org.objenesis;version="[3.1,4.0)", \
                    org.opentest4j.*;resolution:=optional, \
                    org.mockito.*

                # Don't add the Private-Package header.
                # See https://bnd.bndtools.org/instructions/removeheaders.html
                -removeheaders: Private-Package

                # Configures the automatic module name for Java 9+.
                Automatic-Module-Name: org.mockito

                # Don't add all the extra headers bnd normally adds.
                # See https://bnd.bndtools.org/instructions/noextraheaders.html
                -noextraheaders: true
            """.trimIndent())
        }
    }

    test {
        val java11 = providers.environmentVariable("SIMULATE_JAVA11")
        if (java11.isPresent) {
            logger.info("$path - use Java11 simulation: $java11")
            systemProperty("org.mockito.internal.noUnsafeInjection", java11.get())
        }
    }

    // This task is used to generate Mockito extensions for testing see ci.yml build matrix.
    val createTestResources by registering {
        val mockitoExtConfigFiles = listOf(
            mockitoExtensionConfigFile(project, "org.mockito.plugins.MockMaker"),
            mockitoExtensionConfigFile(project, "org.mockito.plugins.MemberAccessor"),
        )
        outputs.files(mockitoExtConfigFiles)
        doLast {
            // Configure MockMaker from environment (if specified), otherwise use default
            configureMockitoExtensionFromCi(project, "org.mockito.plugins.MockMaker", "MOCK_MAKER")

            // Configure MemberAccessor from environment (if specified), otherwise use default
            configureMockitoExtensionFromCi(project, "org.mockito.plugins.MemberAccessor", "MEMBER_ACCESSOR")
        }
    }
    processTestResources {
        dependsOn(createTestResources)
    }

    val removeTestResources by registering {
        val mockitoExtConfigFiles = listOf(
            mockitoExtensionConfigFile(project, "org.mockito.plugins.MockMaker"),
            mockitoExtensionConfigFile(project, "org.mockito.plugins.MemberAccessor"),
        )
        outputs.files(mockitoExtConfigFiles)
        doLast {
            mockitoExtConfigFiles.forEach {
                if (it.exists()) {
                    it.delete()
                }
            }
        }
    }
    test {
        finalizedBy(removeTestResources)
    }
}

fun Task.configureMockitoExtensionFromCi(
    project: Project,
    mockitoExtension: String,
    ciMockitoExtensionEnvVarName: String,
) {
    val mockitoExtConfigFile = mockitoExtensionConfigFile(project, mockitoExtension)
    val mockMaker = project.providers.environmentVariable(ciMockitoExtensionEnvVarName)
    if (mockMaker.isPresent && !mockMaker.get().endsWith("default")) {
        logger.info("Using $mockitoExtension ${mockMaker.get()}")
        mockitoExtConfigFile.run {
            parentFile.mkdirs()
            createNewFile()
            writeText(mockMaker.get())
        }
    } else {
        logger.info("Using default $mockitoExtension")
    }
}

fun mockitoExtensionConfigFile(project: Project, mockitoExtension: String) =
    file("${project.sourceSets.test.get().output.resourcesDir}/mockito-extensions/$mockitoExtension")
