import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.VERIFICATION
import org.gradle.api.attributes.TestSuiteName.TEST_SUITE_NAME_ATTRIBUTE
import org.gradle.api.attributes.VerificationType.JACOCO_RESULTS
import org.gradle.api.attributes.VerificationType.VERIFICATION_TYPE_ATTRIBUTE

plugins {
    id("mockito.coverage-conventions")
}

pluginManager.withPlugin("com.android.application") {
    configurations.consumable("androidCoverageElements") {
        attributes {
            attribute(CATEGORY_ATTRIBUTE, objects.named(VERIFICATION))
            attribute(TEST_SUITE_NAME_ATTRIBUTE, objects.named("test"))
            attribute(VERIFICATION_TYPE_ATTRIBUTE, objects.named(JACOCO_RESULTS))
        }
        outgoing.artifact(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected"))
        outgoing.artifact(layout.buildDirectory.dir("outputs/unit_test_code_coverage/debugUnitTest")) {
            builtBy(tasks.withType<Test>().named { it == "testDebugUnitTest" })
        }
    }
}
