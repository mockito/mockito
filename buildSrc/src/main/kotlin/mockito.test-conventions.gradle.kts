import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    `jvm-toolchains`
}

tasks {
    /**
     * Configure the main test task
     */
    // use the named `test` task, to be able to configure it for both `java` and `com.android.application` projects
    named<Test>("test") {
        include("**/*Test.class")

        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            showCauses = true
        }

        if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)
            && System.getenv("MEMBER_ACCESSOR") == "member-accessor-reflection") {
            jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
        }
    }



    /**
     * Apply the CI test launcher configuration to any test tasks.
     */
    withType<Test> {
        javaLauncher = javaToolchains.launcherFor {
            languageVersion = providers
                .gradleProperty("mockito.test.java")
                .map {
                    if (it == "auto") {
                        JavaLanguageVersion.of(JavaVersion.current().majorVersion)
                    } else {
                        JavaLanguageVersion.of(it)
                    }
                }
        }
    }
}
