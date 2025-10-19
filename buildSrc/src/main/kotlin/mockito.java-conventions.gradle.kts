import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

// only apply to Java projects
// i.e., do nothing when applied to Android projects
plugins.withType(JavaPlugin::class) {
    val javaExt = the<JavaPluginExtension>()
    // Derive release from toolchain when present, otherwise fall back to the
    // extension's targetCompatibility (or 11 if not customized).
    val releaseProvider = javaExt.toolchain.languageVersion.map { it.asInt() }
        .orElse(providers.provider {
            // Use targetCompatibility when explicitly set; default to 11
            (javaExt.targetCompatibility.majorVersion.toIntOrNull() ?: 11)
        })

    tasks {
        withType<JavaCompile>().configureEach {
            // I don't believe those warnings add value given modern IDEs
            options.isWarnings = false
            options.encoding = "UTF-8"
            // Use javac --release to ensure proper API targeting
            options.release.set(releaseProvider)
        }

        withType<Javadoc>().configureEach {
            options {
                encoding = "UTF-8"
                // Match the Javadoc source level to the chosen release
                source = releaseProvider.get().toString()

                if (this is StandardJavadocDocletOptions) {
                    addStringOption("Xdoclint:none", "-quiet")
                    addStringOption("charSet", "UTF-8")
                }
            }
        }
    }
}

