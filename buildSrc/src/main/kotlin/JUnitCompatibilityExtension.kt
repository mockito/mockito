import javax.inject.Inject

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

/**
 * Declares JUnit versions and test JVMs for classpath and Equinox compatibility tests.
 * The convention supplies junit5 (JVM 11) and junit6 (JVM 17), which can be configured by name.
 */
abstract class JUnitCompatibilityExtension @Inject constructor(objects: ObjectFactory) {
    val runtimes = objects.domainObjectContainer(JUnitRuntime::class)
}
