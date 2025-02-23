import aQute.bnd.gradle.Resolve

plugins {
    id("mockito.library-conventions")
    id("mockito.javadoc-conventions")
}

description = "Mockito JUnit 5 support"

dependencies {
    api(project(":mockito-core"))
    implementation(libs.junit.jupiter.api)
    testImplementation(libs.assertj)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

mockitoJavadoc {
    title = "Mockito JUnit Jupiter ${project.version} API"
    docTitle = """<h1>Mockito JUnit Jupiter ${project.version} API.</h1>"""
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    jar {
        bundle { // this: BundleTaskExtension
            classpath = project.configurations.runtimeClasspath.get()
            bnd("""
                # Bnd instructions for the Mockito Junit Jupiter.

                # Set a bundle name slightly different from project description.
                Bundle-Name: Mockito Extension Library for JUnit 5.

                # Set the Bundle-SymbolicName to "org.mockito.junit-jupiter".
                # Otherwise bnd plugin will use archiveClassifier by default.
                Bundle-SymbolicName: org.mockito.junit-jupiter

                # Version rules for the bundle.
                # https://bnd.bndtools.org/macros/version_cleanup.html
                Bundle-Version: ${'$'}{version_cleanup;${project.version}}

                # Set bundle license
                Bundle-License: MIT

                # Export rules for public and internal packages
                # https://bnd.bndtools.org/heads/export_package.html
                Export-Package: org.mockito.junit.jupiter.*;version=${archiveVersion.get()}

                # Don't add the Private-Package header.
                # See https://bnd.bndtools.org/instructions/removeheaders.html
                -removeheaders: Private-Package

                # Don't add all the extra headers bnd normally adds.
                # See https://bnd.bndtools.org/instructions/noextraheaders.html
                -noextraheaders: true

                # Instruct the APIGuardianAnnotations how to operate.
                # See https://bnd.bndtools.org/instructions/export-apiguardian.html
                -export-apiguardian: org.mockito.internal.*
            """.trimIndent())
        }
    }

    // Bnd's Resolve task uses a properties file for its configuration. This
    // task writes out the properties necessary for it to verify the OSGi
    // metadata.
    val osgiProperties by registering(WriteProperties::class) {
        destinationFile.set(layout.buildDirectory.file("verifyOSGiProperties.bndrun"))
        property("-standalone", true)
        property("-runee", "JavaSE-${java.targetCompatibility}")
        property("-runrequires", "osgi.identity;filter:=\"(osgi.identity=org.mockito.junit-jupiter)\"")
    }

    // Bnd's Resolve task is what verifies that a jar can be used in OSGi and
    // that its metadata is valid. If the metadata is invalid, this task will
    // fail.
    val verifyOSGi by registering(Resolve::class) {
        bndrun = osgiProperties.flatMap { it.destinationFile }
        outputBndrun = layout.buildDirectory.file("resolvedOSGiProperties.bndrun")
        isReportOptional = false
    }

    check {
        dependsOn(verifyOSGi)
    }

    javadoc {
        // Handle https://bugs.openjdk.org/browse/JDK-8274639
        // Note: Might be solved by a toolchain launcher for the javadoc.
        if (JavaVersion.current() >= JavaVersion.VERSION_18) {
            javadocDocletOptions {
                addStringOption("-link-modularity-mismatch", "info")
                links("https://junit.org/junit5/docs/${libs.versions.junit.jupiter.get()}/api/")
            }
        } else {
            logger.info("Javadoc tool below 18, links to JUnit Jupiter javadocs was not added.")
        }
    }
}
