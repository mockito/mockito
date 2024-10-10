import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.kotlin.dsl.checkstyle
import org.gradle.kotlin.dsl.withType

plugins {
    checkstyle
}

checkstyle {
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.stylesheet = resources.text.fromFile("$rootDir/config/checkstyle/checkstyle.xsl")
    }
}
