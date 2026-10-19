import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
}

val junit6RuntimeOnly = configurations.dependencyScope("junit6RuntimeOnly")
val junit6RuntimeClasspath = configurations.resolvable("junit6RuntimeClasspath") {
    extendsFrom(
        configurations.testImplementation.get(),
        configurations.testRuntimeOnly.get(),
        junit6RuntimeOnly.get(),
    )
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
    }
}

dependencies {
    add(junit6RuntimeOnly.name, platform(libs.junit6.bom))
}

tasks.register<Test>("junit6Test") {
    group = "verification"
    description = "Runs the existing Jupiter tests against JUnit 6 on Java 17."

    // Compile against JUnit 5 and reuse the same classes to check binary compatibility.
    testClassesDirs = files(sourceSets.test.map { it.output.classesDirs })
    classpath = files(sourceSets.test.map { it.output }, sourceSets.main.map { it.output }, junit6RuntimeClasspath)
    include("**/*Test.class")
    useJUnitPlatform()
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
