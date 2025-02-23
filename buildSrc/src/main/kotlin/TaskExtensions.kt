import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

/** Applies the standard doclet options. */
fun Javadoc.javadocDocletOptions(block: StandardJavadocDocletOptions.() -> Unit) {
    (options as StandardJavadocDocletOptions).apply(block)
}
