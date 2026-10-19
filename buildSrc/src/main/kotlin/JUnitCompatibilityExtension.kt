import javax.inject.Inject

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

/** Declares JUnit versions and test JVMs for classpath and Equinox compatibility tests. */
abstract class JUnitCompatibilityExtension @Inject constructor(objects: ObjectFactory) {
    val runtimes = objects.domainObjectContainer(JUnitRuntime::class)
}
