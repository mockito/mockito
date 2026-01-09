import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

/**
 * Java 21 policy:
 * - Only run when explicitly requested via -Pmockito.test.java=21
 * - Otherwise skip compilation/tests and log why
 */

val requiredJavaVersion = 21

val requestedJavaVersion = providers.gradleProperty("mockito.test.java").orElse("auto")
val shouldRun = requestedJavaVersion.map { it == requiredJavaVersion.toString() }

val logJavaSkip by tasks.registering {
    doLast {
        val requested = requestedJavaVersion.get()
        if (requested != requiredJavaVersion.toString()) {
            logger.lifecycle(
                "${project.name} runs only with -Pmockito.test.java=$requiredJavaVersion. " +
                    "mockito.test.java was set to $requested, skipping these tests."
            )
        }
    }
}

tasks.matching { it.name == "check" }.configureEach {
    dependsOn(logJavaSkip)
}

plugins.withType(JavaPlugin::class) {
    val toolchains = extensions.getByType(JavaToolchainService::class.java)
    val javaExt = extensions.getByType(JavaPluginExtension::class.java)

    javaExt.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_21
    javaExt.targetCompatibility = org.gradle.api.JavaVersion.VERSION_21
    javaExt.toolchain {
        languageVersion.set(JavaLanguageVersion.of(requiredJavaVersion))
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        onlyIf { shouldRun.get() }
        options.release.set(requiredJavaVersion)
    }

    tasks.withType(Test::class.java).configureEach {
        onlyIf { shouldRun.get() }
        javaLauncher.set(
            toolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(requiredJavaVersion))
            }
        )
    }
}
