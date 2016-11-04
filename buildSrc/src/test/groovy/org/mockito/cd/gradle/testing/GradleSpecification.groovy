package org.mockito.cd.gradle.testing

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.mockito.workflow.gradle.internal.ReleaseWorkflowPlugin
import spock.lang.Specification

/**
 * Allows convenient testing of Gradle plugins:
 *  - automated addition of the classpath without any extra Gradle tasks in project's build.gradle file
 *  - convenience methods for running / failing tasks and printing the build.gradle file contents
 */
class GradleSpecification extends Specification {

    @Rule
    final TemporaryFolder projectDir = new TemporaryFolder()

    File buildFile

    private final static File CLASSES_DIR = findClassesDir();
    private final static File RESOURCES_DIR = findResourcesDir();

    void setup() {
        buildFile = projectDir.newFile('build.gradle')
        buildFile << """buildscript {
            dependencies {
                classpath files("${CLASSES_DIR.absolutePath}")
                classpath files("${RESOURCES_DIR.absolutePath}")
            }
        }"""
    }

    /**
     * Runs Gradle with given arguments, prints the build.gradle file to the standard output if the test fails
     */
    BuildResult pass(String... args) {
        try {
            GradleRunner.create()
                    .withProjectDir(projectDir.root)
                    .withArguments(args)
                    .build()
        } catch (Exception e) {
            println " ---- build.gradle ---- \n" + buildFile.text + "\n ------------------------"
            throw e
        }
    }

    /**
     * Runs Gradle with given arguments, expects it to FAIL,
     * prints the build.gradle file to the standard output if the test fails
     */
    BuildResult fail(String... args) {
        try {
            GradleRunner.create()
                    .withProjectDir(projectDir.root)
                    .withArguments(args)
                    .buildAndFail()
        } catch (Exception e) {
            println " ---- build.gradle ---- \n" + buildFile.text + "\n ------------------------"
            throw e
        }
    }

    private static File findClassesDir() {
        //Using one of the production classes to find directory where IDE or the build system outputs compiled production code
        def bearing = ReleaseWorkflowPlugin.class.name.replaceAll("\\.", "/") + ".class"
        return findDir(bearing)
    }

    private static File findResourcesDir() {
        //Using standard location of gradle plugins to find where production resources are outputted by IDE or build system
        def bearing = "META-INF/gradle-plugins"
        return findDir(bearing)
    }

    private static File findDir(String bearing) {
        //Based on a bearing resource, we're finding root directory in the classpath
        def gradlePluginsDir = GradleSpecification.classLoader.getResource(bearing).file
        def classesDir = gradlePluginsDir - bearing
        def result = new File(classesDir)
        assert result.isDirectory()
        return result
    }
}
