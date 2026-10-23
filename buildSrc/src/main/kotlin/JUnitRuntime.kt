import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Property

abstract class JUnitRuntime @Inject constructor(private val runtimeName: String) : Named {
    override fun getName(): String = runtimeName

    abstract val bom: Property<MinimalExternalModuleDependency>

    /** JVM version used to run the tests. Compilation uses the project's Java baseline. */
    abstract val testJvmVersion: Property<Int>
}
