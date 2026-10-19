import aQute.bnd.gradle.Bundle
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
    id("mockito.osgi-conventions")
}

val junitCompatibility = extensions.create<JUnitCompatibilityExtension>("junitCompatibility")

val osgiTest = sourceSets.create("osgiTest")
val osgiTestBundle = sourceSets.create("osgiTestBundle")
val libraries = extensions.getByType<VersionCatalogsExtension>().named("libs")

junitCompatibility.runtimes {
    register("junit5") {
        bom.convention(libraries.findLibrary("junit5-bom").get())
        testJvmVersion.convention(11)
    }
    register("junit6") {
        bom.convention(libraries.findLibrary("junit6-bom").get())
        testJvmVersion.convention(17)
    }
}

// Compile against the existing test baseline; each runtime BOM selects the installed JUnit bundles.
configurations.named(osgiTestBundle.compileClasspathConfigurationName) {
    extendsFrom(configurations.testImplementation.get(), configurations.testRuntimeOnly.get())
}

dependencies {
    // Keep the Equinox test runner separate from the libraries installed as bundles.
    add(osgiTest.implementationConfigurationName, libraries.findLibrary("junit4").get())
    add(osgiTest.implementationConfigurationName, libraries.findLibrary("osgi").get())
    add(osgiTest.runtimeOnlyConfigurationName, libraries.findLibrary("equinox").get())

    add(osgiTestBundle.compileOnlyConfigurationName, libraries.findLibrary("osgi").get())
    // dependency version will be given by the BOM via the junitCompatibility extension
    add(osgiTestBundle.implementationConfigurationName, "org.junit.jupiter:junit-jupiter-api")
    add(osgiTestBundle.runtimeOnlyConfigurationName, "org.junit.jupiter:junit-jupiter-engine")
    add(osgiTestBundle.runtimeOnlyConfigurationName, "org.junit.platform:junit-platform-launcher")
}

val testBundle = tasks.register<Bundle>("osgiTestBundle") {
    description = "Packages the Jupiter compatibility tests as an OSGi bundle."
    archiveClassifier = "osgi-tests"
    from(osgiTestBundle.output)
    bundle {
        classpath = osgiTestBundle.compileClasspath
        bnd("""
            Bundle-SymbolicName: mockito.junit.compatibility.tests
            -exportcontents: *
            Import-Package: org.junit.jupiter.*;version="[5.13,7)", org.junit.platform.*;version="[1.13,7)", *
            """.trimIndent())
    }
}

junitCompatibility.runtimes.all {
    val runtime = this
    val launcher = javaToolchains.launcherFor {
        languageVersion = runtime.testJvmVersion.map(JavaLanguageVersion::of)
    }
    val runtimeOnly = configurations.dependencyScope("${name}RuntimeOnly")
    val runtimeClasspath = configurations.resolvable("${name}RuntimeClasspath") {
        extendsFrom(
            configurations.testImplementation.get(),
            configurations.testRuntimeOnly.get(),
            runtimeOnly.get(),
        )
    }

    dependencies {
        addProvider(runtimeOnly.name, platform(runtime.bom))
    }

    val testTask = tasks.register<Test>("${name}Test") {
        group = "verification"
        description = "Runs the existing Jupiter tests against ${runtime.name}."

        // Compile against the baseline and reuse the same classes to check binary compatibility.
        testClassesDirs = files(sourceSets.test.map { it.output.classesDirs })
        classpath = files(
            sourceSets.test.map { it.output },
            sourceSets.main.map { it.output },
            runtimeClasspath
        )
        include("**/*Test.class")
        useJUnitPlatform()
        javaLauncher = launcher
    }

    val bundles = configurations.resolvable("${name}OsgiBundles") {
        extendsFrom(
            configurations.getByName(osgiTestBundle.implementationConfigurationName),
            configurations.getByName(osgiTestBundle.runtimeOnlyConfigurationName),
            runtimeOnly.get(),
        )
    }

    listOf(runtimeClasspath, bundles).forEach {
        it.configure {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
                attributeProvider(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, runtime.testJvmVersion)
            }
        }
    }

    val osgiTestTask = tasks.register<Test>("${name}OsgiTest") {
        group = "verification"
        description = "Runs the Jupiter test bundle in Equinox against ${runtime.name}."
        testClassesDirs = osgiTest.output.classesDirs
        classpath = osgiTest.runtimeClasspath
        useJUnit()
        javaLauncher = launcher
        jvmArgumentProviders.add(OsgiBundlesArgumentProvider(files(bundles, testBundle), runtime.bom.map { it.version!! }))
    }

    tasks.named("check") {
        dependsOn(testTask, osgiTestTask)
    }
}
