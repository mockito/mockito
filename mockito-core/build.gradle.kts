import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ModuleVisitor
import org.objectweb.asm.Opcodes

plugins {
    id("mockito.library-conventions")
    id("mockito.java-backward-compatibility-checks-conventions")
    id("mockito.javadoc-conventions")
    id("java-test-fixtures")
}

description = "Mockito mock objects library core API and implementation"
base.archivesName = "mockito-core"

apply {
    from(rootProject.file("gradle/coverage.gradle"))
}

configurations {
    // Putting 'provided' dependencies on testCompileOnly and testRuntimeOnly classpath.
    testCompileOnly.configure {
        extendsFrom(compileOnly.get())
    }
    testRuntimeOnly.configure {
        extendsFrom(compileOnly.get())
    }
}

// Disables publishing test fixtures:
// https://docs.gradle.org/current/userguide/java_testing.html#ex-disable-publishing-of-test-fixtures-variants
with(components["java"] as AdhocComponentWithVariants) {
    withVariantsFromConfiguration(configurations.testFixturesApiElements.get()) { skip() }
    withVariantsFromConfiguration(configurations.testFixturesRuntimeElements.get()) { skip() }
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

    testFixturesImplementation(libs.assertj)
    testFixturesImplementation(libs.objenesis)
    testFixturesCompileOnly(libs.junit4)
}

mockitoJavadoc {
    title = "Mockito ${project.version} API"
    docTitle =
        """<h1><a href="org/mockito/Mockito.html">Click to see examples</a>. Mockito ${project.version} API.</h1>"""
}

val generatedInlineResource: Provider<Directory> = layout.buildDirectory.dir("generated/resources/inline/java")
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

        from(sourceSets.main.flatMap { it.java.classesDirectory }
            .map { it.file("org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.class") })
        into(generatedInlineResource.map { it.dir("org/mockito/internal/creation/bytebuddy") })

        rename(".*", "inject-MockMethodDispatcher.raw")
    }

    val removeInjectionPackageFromModuleInfo by registering(DefaultTask::class) {
        dependsOn(compileJava)

        doLast {
            val moduleInfo = sourceSets.main.get().output.classesDirs.first().resolve("module-info.class")

            val reader = ClassReader(moduleInfo.readBytes())
            val writer = ClassWriter(reader, 0)
            reader.accept(object : ClassVisitor(Opcodes.ASM9, writer) {
                override fun visitModule(name: String?, access: Int, version: String?): ModuleVisitor {
                    return object : ModuleVisitor(
                        Opcodes.ASM9,
                        super.visitModule(name, access, version)
                    ) {
                        override fun visitPackage(packaze: String) {
                            if (packaze != "org/mockito/internal/creation/bytebuddy/inject") {
                                super.visitPackage(packaze)
                            }
                        }
                    }
                }
            }, 0)

            moduleInfo.writeBytes(writer.toByteArray())
        }
    }

    classes.dependsOn(copyMockMethodDispatcher)
    classes.dependsOn(removeInjectionPackageFromModuleInfo)


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
            bnd(
                """
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
                    !org.mockito.internal.creation.bytebuddy.inject,org.mockito.internal.*;status=INTERNAL;mandatory:=status;version=${archiveVersion.get()}, \
                    !org.mockito.internal.creation.bytebuddy.inject,org.mockito.*;version=${archiveVersion.get()}

                # General rules for package import
                # https://bnd.bndtools.org/heads/import_package.html
                # Override the default version policy to allow for a range of versions.
                # At this point it's unclear whether `versionpolicy` is deprecated, here's
                # some relevant pages on versions
                # https://bnd.bndtools.org/chapters/170-versioning.html
                # https://bnd.bndtools.org/macros/version.html
                # https://bnd.bndtools.org/macros/versionmask.html
                # https://bnd.bndtools.org/macros/range.html
                -versionpolicy: [${'$'}{version;==;${'$'}{@}},${'$'}{version;+;${'$'}{@}})
                Import-Package: \
                    net.bytebuddy.*;version="[1.6.0,2.0)", \
                    junit.*;resolution:=optional, \
                    org.junit.*;resolution:=optional, \
                    org.hamcrest;resolution:=optional, \
                    org.objenesis;version="[3.1,4.0)", \
                    org.opentest4j.*;resolution:=optional, \
                    !org.mockito.internal.creation.bytebuddy.inject,org.mockito.*

                # Don't add the Private-Package header.
                # See https://bnd.bndtools.org/instructions/removeheaders.html
                -removeheaders: Private-Package

                # Don't add all the extra headers bnd normally adds.
                # See https://bnd.bndtools.org/instructions/noextraheaders.html
                -noextraheaders: true
            """.trimIndent()
            )
        }
    }

    test {
        val java11 = providers.environmentVariable("SIMULATE_JAVA11")
        if (java11.isPresent) {
            logger.info("$path - use Java11 simulation: $java11")
            systemProperty("org.mockito.internal.noUnsafeInjection", java11.get())
        }
    }
}

//<editor-fold defaultstate="collapsed" desc="Environment-based Mockito extension configuration for tests">
val mockitoExtConfigFiles = listOf(
    mockitoExtensionConfigFile(project, "org.mockito.plugins.MockMaker"),
    mockitoExtensionConfigFile(project, "org.mockito.plugins.MemberAccessor"),
)

val createTestResources by tasks.registering {
    outputs.files(mockitoExtConfigFiles)
    doLast {
        // Configure MockMaker from environment (if specified), otherwise use default
        configureMockitoExtensionFromCi(project, "org.mockito.plugins.MockMaker", "MOCK_MAKER")

        // Configure MemberAccessor from environment (if specified), otherwise use default
        configureMockitoExtensionFromCi(project, "org.mockito.plugins.MemberAccessor", "MEMBER_ACCESSOR")
    }
}

sourceSets.test {
    resources {
        output.dir(
            layout.buildDirectory.dir("generated/resources/ext-config/test"),
            "builtBy" to createTestResources
        )
    }
}

fun Task.configureMockitoExtensionFromCi(
    project: Project,
    mockitoExtension: String,
    ciMockitoExtensionEnvVarName: String,
) {
    val mockitoExtConfigFile = mockitoExtensionConfigFile(project, mockitoExtension)
    val extEnvVar = project.providers.environmentVariable(ciMockitoExtensionEnvVarName)
    if (extEnvVar.isPresent && !extEnvVar.get().endsWith("default")) {
        logger.info("Using $mockitoExtension ${extEnvVar.get()}")
        mockitoExtConfigFile.run {
            parentFile.mkdirs()
            createNewFile()
            writeText(extEnvVar.get())
        }
    } else {
        logger.info("Using default $mockitoExtension")
    }
}

fun mockitoExtensionConfigFile(project: Project, mockitoExtension: String) =
    file(project.layout.buildDirectory.dir("generated/resources/ext-config/test/mockito-extensions/$mockitoExtension"))
//</editor-fold>

