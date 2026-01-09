import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

/**
 * Java 17+ policy:
 * - If -Pmockito.test.java requests < 17, override to 17
 * - Log the override and wire it into compile/test tasks
 */
val minJavaVersion = 17

val requestedJavaVersion: Provider<Int> = providers.requestedJavaVersion()
val effectiveJavaVersion: Provider<Int> =
    requestedJavaVersion.map { requested: Int -> maxOf(requested, minJavaVersion) }

val logJavaOverride by tasks.registering {
    doLast {
        val requested = requestedJavaVersion.get()
        if (requested < minJavaVersion) {
            logger.lifecycle(
                "${project.path} requires Java $minJavaVersion+, " +
                        "but mockito.test.java was set to $requested. Overriding to $minJavaVersion."
            )
        }
    }
}

plugins.withType(JavaPlugin::class) {
    val toolchains = extensions.getByType(JavaToolchainService::class.java)
    val javaExt = extensions.getByType(JavaPluginExtension::class.java)

    javaExt.toolchain {
        languageVersion.set(
            effectiveJavaVersion.map { v: Int -> JavaLanguageVersion.of(v) }
        )
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        dependsOn(logJavaOverride)

        @Suppress("UNCHECKED_CAST")
        options.release.set(effectiveJavaVersion as Provider<Int>)
    }

    tasks.withType(Test::class.java).configureEach {
        dependsOn(logJavaOverride)

        javaLauncher.set(
            toolchains.launcherFor {
                languageVersion.set(
                    effectiveJavaVersion.map { v: Int -> JavaLanguageVersion.of(v) }
                )
            }
        )
    }
}
