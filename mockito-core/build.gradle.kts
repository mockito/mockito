import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("mockito.library-conventions")
    id("mockito.java-backward-compatibility-checks-conventions")
    id("mockito.javadoc-conventions")
}

description = "Mockito mock objects library core API and implementation"
base.archivesName = "mockito-core"

apply {
    from(rootProject.file("gradle/coverage.gradle"))
    from("inline-mock.gradle")
    from("testing.gradle")
}

val testUtil = configurations.create("testUtil") //TODO move to separate project
configurations {
    // Putting 'provided' dependencies on test compile and runtime classpath.
    testCompileOnly.configure {
        extendsFrom(compileOnly.get())
    }
    testRuntimeOnly.configure {
        extendsFrom(compileOnly.get())
    }
}

dependencies {
    api(libs.bytebuddy)
    api(libs.bytebuddy.agent)

    compileOnly(libs.junit4)
    compileOnly(libs.hamcrest)
    compileOnly(libs.opentest4j)
    implementation(libs.objenesis)

    testImplementation(libs.assertj)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)

    testUtil(sourceSets.test.get().output)
}

mockitoJavadoc {
    title = "Mockito ${project.version} API"
    docTitle = """<h1><a href="org/mockito/Mockito.html">Click to see examples</a>. Mockito ${project.version} API.</h1>"""
}

tasks {
    jar {
        bundle { // this: BundleTaskExtension
            classpath(project.configurations.compileClasspath)
            bnd("""
                Bundle-Name: Mockito Mock Library for Java. Core bundle requires Byte Buddy and Objenesis.
                Bundle-SymbolicName: org.mockito.mockito-core
                Bundle-Version: ${'$'}{version_cleanup;${project.version}}
                -versionpolicy: [${'$'}{version;==;${'$'}{@}},${'$'}{version;+;${'$'}{@}})
                Export-Package: org.mockito.internal.*;status=INTERNAL;mandatory:=status;version=${archiveVersion.get()},org.mockito.*;version=${archiveVersion.get()}

                Import-Package: \
                    net.bytebuddy.*;version="[1.6.0,2.0)", \
                    junit.*;resolution:=optional, \
                    org.junit.*;resolution:=optional, \
                    org.hamcrest;resolution:=optional, \
                    org.objenesis;version="[3.1,4.0)", \
                    org.opentest4j.*;resolution:=optional, \
                    org.mockito.*

                -removeheaders: Private-Package
                Automatic-Module-Name: org.mockito
                -noextraheaders: true
            """.trimIndent())
        }
    }
}
