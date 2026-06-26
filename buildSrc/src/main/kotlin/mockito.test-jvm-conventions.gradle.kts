import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

/**
 * Unified JVM test conventions plugin.
 *
 * Projects declare their JVM requirements via the `testJvm` extension:
 *
 *     testJvm {
 *         minJvm.set(17)          // override requested Java upward to this floor
 *         maxJvm.set(21)          // optional: skip tasks if requested Java != maxJvm (exact-match)
 *     }
 *
 * The requested Java version comes from `-Pmockito.test.java` (or "auto" → current JVM).
 */

interface TestJvmExtension {
    /** Minimum JVM version. Requested versions below this are overridden upward. */
    val minJvm: Property<Int>

    /** Optional maximum / exact-match JVM version. When set, tasks are skipped unless requested == maxJvm. */
    val maxJvm: Property<Int>

    /** The effective Java version after applying min/max policy. Read-only for consumers. */
    val effectiveJavaVersion: Property<Int>
}

val ext = extensions.create<TestJvmExtension>("testJvm")
val requestedJavaVersion: Provider<Int> = providers.requestedJavaVersion()

// Wire effectiveJavaVersion as a derived convention
ext.effectiveJavaVersion.convention(
    requestedJavaVersion.map { requested ->
        val min = ext.minJvm.getOrElse(requested)
        maxOf(requested, min)
    }
)

val effectiveJavaVersion: Provider<Int> = ext.effectiveJavaVersion

// Whether tasks should run (exact-match semantics when maxJvm is set)
val shouldRun: Provider<Boolean> = requestedJavaVersion.map { requested ->
    if (ext.maxJvm.isPresent) {
        requested == ext.maxJvm.get()
    } else {
        true
    }
}

val logJvmPolicy by tasks.registering {
    doLast {
        val requested = requestedJavaVersion.get()
        val min = ext.minJvm.getOrElse(requested)
        val max = ext.maxJvm.orNull

        if (max != null && requested != max) {
            logger.lifecycle(
                "${project.name} runs only with -Pmockito.test.java=$max. " +
                    "mockito.test.java was set to $requested, skipping these tests."
            )
        } else if (requested < min) {
            logger.lifecycle(
                "${project.path} requires Java $min+, " +
                    "but mockito.test.java was set to $requested. Overriding to $min."
            )
        }
    }
}

tasks.matching { it.name == "check" }.configureEach {
    dependsOn(logJvmPolicy)
}

plugins.withType(JavaPlugin::class) {
    val toolchains = extensions.getByType(JavaToolchainService::class.java)
    val javaExt = extensions.getByType(JavaPluginExtension::class.java)

    javaExt.toolchain {
        languageVersion.set(
            effectiveJavaVersion.map { v -> JavaLanguageVersion.of(v) }
        )
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        dependsOn(logJvmPolicy)
        onlyIf { shouldRun.get() }

        @Suppress("UNCHECKED_CAST")
        options.release.set(effectiveJavaVersion as Provider<Int>)
    }

    tasks.withType(Test::class.java).configureEach {
        dependsOn(logJvmPolicy)
        onlyIf { shouldRun.get() }

        javaLauncher.set(
            toolchains.launcherFor {
                languageVersion.set(
                    effectiveJavaVersion.map { v -> JavaLanguageVersion.of(v) }
                )
            }
        )
    }
}
