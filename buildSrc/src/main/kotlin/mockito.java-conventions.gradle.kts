import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

// only apply to Java projects
// i.e., do nothing when applied to Android projects
plugins.withType(JavaPlugin::class) {
    val javaReleaseVersion = 11

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.toVersion(javaReleaseVersion)
        targetCompatibility = JavaVersion.toVersion(javaReleaseVersion)
    }

    tasks {
        withType<JavaCompile>().configureEach {
            // I don't believe those warnings add value given modern IDEs
            options.isWarnings = false
            options.encoding = "UTF-8"
        }

        withType<Javadoc>().configureEach {
            options {
                encoding = "UTF-8"
                source = javaReleaseVersion.toString()

                if (this is StandardJavadocDocletOptions) {
                    addStringOption("Xdoclint:none", "-quiet")
                    addStringOption("charSet", "UTF-8")
                }
            }
        }
    }
}

