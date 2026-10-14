import org.gradle.api.provider.Property

interface MockitoJavadocExtension {
    val title: Property<String>
    val docTitle: Property<String>
}
