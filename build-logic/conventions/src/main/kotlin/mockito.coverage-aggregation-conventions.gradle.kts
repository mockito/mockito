import org.gradle.api.attributes.Category
import org.gradle.api.attributes.TestSuiteName
import org.gradle.api.attributes.VerificationType
import org.gradle.testing.jacoco.plugins.JacocoCoverageReport

plugins {
    id("jacoco-report-aggregation")
    id("mockito.coverage-conventions")
}

// AGP does not publish the JVM coverage variants consumed by the aggregation plugin.
val androidCoverage = configurations.dependencyScope("androidCoverage")
val androidCoverageResults = configurations.resolvable("androidCoverageResults") {
    extendsFrom(androidCoverage.get())
    isTransitive = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.VERIFICATION))
        attribute(TestSuiteName.TEST_SUITE_NAME_ATTRIBUTE, objects.named("test"))
        attribute(VerificationType.VERIFICATION_TYPE_ATTRIBUTE, objects.named(VerificationType.JACOCO_RESULTS))
    }
}

val mockitoCoverage = reporting.reports.create<JacocoCoverageReport>("mockitoCoverage") {
    testSuiteName = "test"

    reportTask.configure {
        description = "Generates aggregate coverage for Mockito and its integration tests."

        val allClasses = objects.fileCollection().from(classDirectories.from.toList())
        classDirectories.setFrom(allClasses.asFileTree.matching {
            exclude("**/internal/util/concurrent/**")
        })

        executionData.from(androidCoverageResults.map { results ->
            results.asFileTree.matching {
                include("*/coverage.ec", "testDebugUnitTest.exec")
            }
        })

        reports {
            xml.required = true
            html.required = true
            html.outputLocation = rootProject.layout.buildDirectory.dir("jacocoHtml")
        }

        doLast {
            logger.info("Jacoco HTML created: ${reports.html.outputLocation.get().file("index.html").asFile.toURI()}")
        }
    }
}

// Android uses the same JaCoCo convention but supplies execution data
// under a different configuration.
rootProject.subprojects.forEach {
    it.pluginManager.withPlugin("mockito.coverage-conventions") {
        it.pluginManager.withPlugin("java") {
            dependencies.add("jacocoAggregation", project(it.path))
        }
    }

    it.pluginManager.withPlugin("mockito.android-coverage-conventions") {
        it.pluginManager.withPlugin("com.android.application") {
            dependencies.add("androidCoverage", project(it.path))
            mockitoCoverage.reportTask.configure {
                mustRunAfter("${it.path}:connectedDebugAndroidTest")
            }
        }
    }
}

tasks.register("coverageReport") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Generates the aggregate coverage report for Codecov."
    dependsOn(mockitoCoverage.reportTask)
}
