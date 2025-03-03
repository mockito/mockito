import org.gradle.api.tasks.javadoc.Javadoc

plugins {
    java
}

val mockitoJavadocExtension = project.extensions.create<MockitoJavadocExtension>("mockitoJavadoc").apply {
    title.convention(project.name)
    docTitle.convention("<h1>${project.name}</h1>")
}

val javadocConfigDir = "$rootDir/config/javadoc"

tasks.named<Javadoc>("javadoc") {
    inputs.dir(javadocConfigDir)
    description = "Creates javadoc html for ${project.name}."

    // Work-around as suggested in https://github.com/gradle/gradle/issues/19726
    val sourceSetDirectories = sourceSets.main.get().java.sourceDirectories.joinToString(":")
    val coreOptions = options as CoreJavadocOptions
    coreOptions.addStringOption("-source-path", sourceSetDirectories)
    exclude("**/internal/**")

    // For more details on the format
    // see https://docs.oracle.com/en/java/javase/21/javadoc/javadoc.html
    options {
        title = mockitoJavadocExtension.title.get()
        encoding = "UTF-8"

        if (this is StandardJavadocDocletOptions) {
            addBooleanOption("-allow-script-in-comments", true)
            addFileOption("-add-stylesheet", project.file("$javadocConfigDir/resources/mockito-theme.css"))
            addStringOption("Xwerror", "-quiet")
            charSet = "UTF-8"
            docEncoding = "UTF-8"
            docTitle = mockitoJavadocExtension.docTitle.get()

            bottom(
                """
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/styles/obsidian.min.css">
                <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.9.0/highlight.min.js"></script>
                <script>hljs.highlightAll();</script>
                """.trimIndent().replace("\r|\n|[ ]{8}".toRegex(), "")
            )

            group("Main package", "org.mockito")
            links("https://junit.org/junit4/javadoc/${project.libs.versions.junit4.get()}/")
            linksOffline(
                "https://docs.oracle.com/en/java/javase/11/docs/api/",
                "$javadocConfigDir/jdk-package-list"
            )

            isSplitIndex = true
            isUse = true
            windowTitle = mockitoJavadocExtension.title.get()

            addBooleanOption("html5", true)
        }
        memberLevel = JavadocMemberLevel.PROTECTED
        outputLevel = JavadocOutputLevel.QUIET
    }

    doLast {
        copy {
            from("$javadocConfigDir/resources")
            into(destinationDir!!)
        }
    }
}
