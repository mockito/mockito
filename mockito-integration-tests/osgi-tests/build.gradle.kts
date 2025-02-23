import aQute.bnd.gradle.Bundle

plugins {
    id("java")
    id("mockito.test-conventions")
    id("mockito.osgi-conventions")
}

description = "Test suite for OSGi framework with Mockito"

dependencies {
    testImplementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.osgi)

    testRuntimeOnly(libs.equinox)
}

val testRuntimeBundles: Configuration by configurations.creating

val (otherBundle, otherBundleTask) = configureOsgiBundle(project, "otherBundle")
val (testBundle, testBundleTask) = configureOsgiBundle(project, "testBundle") {
    add("testBundleImplementation", project(":mockito-core"))
    add("testBundleImplementation", project.libs.junit4)
    add("testBundleImplementation", otherBundle.output)
}

dependencies {
    testRuntimeBundles(project(":mockito-core"))
    testRuntimeBundles(libs.bytebuddy)
    testRuntimeBundles(libs.objenesis)

    testRuntimeBundles(testBundleTask.map { it.outputs.files })
    testRuntimeBundles(otherBundleTask.map { it.outputs.files })
}

tasks {
    test {
        jvmArgumentProviders.add(RuntimeBundlesProvider(testRuntimeBundles.asFileTree))
        dependsOn(testRuntimeBundles)

        inputs.files(testBundle.allSource)
            .withPathSensitivity(PathSensitivity.RELATIVE)
            .withPropertyName("testBundleSources")

        inputs.files(otherBundle.allSource)
            .withPathSensitivity(PathSensitivity.RELATIVE)
            .withPropertyName("otherBundleSources")
        useJUnit()
    }
}

/**
 * A helper class to pass classpath elements as relative paths. This allows the build
 * to be checked out in different locations on the file system and still hit the cache.
 */
class RuntimeBundlesProvider(
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    val files: FileTree
) : CommandLineArgumentProvider {
    override fun asArguments(): Iterable<String> {
        val absolutePaths = files.files.map { it.absolutePath }
        return listOf("-DtestRuntimeBundles=${absolutePaths.joinToString(separator = File.pathSeparator)}")
    }
}

fun configureOsgiBundle(project: Project, bundleName: String, dependencyConfig: DependencyHandler.() -> Unit = {}): Pair<SourceSet, TaskProvider<Bundle>> {
    val bundle = project.sourceSets.create(bundleName)

    project.dependencies {
        dependencyConfig()
    }

    val bundleTask = project.tasks.register(bundleName, Bundle::class) {
        archiveAppendix = name
        bundle { // this: BundleTaskExtension
            bnd(
                """
                    Bundle-SymbolicName: $name
                    -exportcontents: *
                    """.trimIndent()
            )
        }
        from(bundle.output)
    }

    return bundle to bundleTask
}

