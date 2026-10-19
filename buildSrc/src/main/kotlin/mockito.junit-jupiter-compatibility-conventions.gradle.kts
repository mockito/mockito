import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
}

val junitCompatibility = extensions.create<JUnitCompatibilityExtension>("junitCompatibility")

junitCompatibility.runtimes.all {
    val runtime = this
    val runtimeOnly = configurations.dependencyScope("${name}RuntimeOnly")
    val runtimeClasspath = configurations.resolvable("${name}RuntimeClasspath") {
        extendsFrom(
            configurations.testImplementation.get(),
            configurations.testRuntimeOnly.get(),
            runtimeOnly.get(),
        )
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
            attributeProvider(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, runtime.testJvmVersion)
        }
    }

    dependencies {
        addProvider(runtimeOnly.name, runtime.bom.map { platform(it) })
    }

    tasks.register<Test>("${name}Test") {
        group = "verification"
        description = "Runs the existing Jupiter tests against ${runtime.name}."

        // Compile against the baseline and reuse the same classes to check binary compatibility.
        testClassesDirs = files(sourceSets.test.map { it.output.classesDirs })
        classpath = files(sourceSets.test.map { it.output }, sourceSets.main.map { it.output }, runtimeClasspath)
        include("**/*Test.class")
        useJUnitPlatform()
        javaLauncher = javaToolchains.launcherFor {
            languageVersion = runtime.testJvmVersion.map(JavaLanguageVersion::of)
        }
    }
}
