import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.FormatterFunc
import com.google.googlejavaformat.java.Formatter
import com.google.googlejavaformat.java.JavaFormatterOptions
import java.io.Serializable

plugins {
    id("com.diffplug.spotless")
}

configure<SpotlessExtension> {
    // We run the check separately on CI, so don't run this by default
    isEnforceCheck = false

    java {
        licenseHeaderFile(rootProject.file("config/spotless/spotless.header"))

        // Not using stock googleJavaFormat due to missing options like reorderModifiers
        // Also, using the Serializable interface to work around bug in spotless 7.0.0
        // https://github.com/diffplug/spotless/issues/2387
        custom("google-java-format", object : Serializable, FormatterFunc {
            override fun apply(input: String): String {
                val formatterOptions = JavaFormatterOptions.builder()
                    .style(JavaFormatterOptions.Style.AOSP)
                    .reorderModifiers(true)
                    .formatJavadoc(false)
                    .build()

                return Formatter(formatterOptions).formatSource(input)
            }
        })

        toggleOffOn()
    }

    format("misc") {
        target(
            "*.gradle",
            "*.gradle.kts",
            ".gitignore",
        )
        targetExclude("**/build/")
        trimTrailingWhitespace()
    }
}

