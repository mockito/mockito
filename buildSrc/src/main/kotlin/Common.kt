import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.the

val Project.libs
    get() = the<LibrariesForLibs>()

val Project.sourceSets
    get() = the<SourceSetContainer>()
