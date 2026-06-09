import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.the
import org.gradle.api.JavaVersion
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

val Project.libs
    get() = the<LibrariesForLibs>()

val Project.sourceSets
    get() = the<SourceSetContainer>()

fun ProviderFactory.requestedJavaVersion(): Provider<Int> =
    gradleProperty("mockito.test.java")
        .orElse("auto")
        .map {
            if (it == "auto") JavaVersion.current().majorVersion.toInt()
            else it.toInt()
        }
