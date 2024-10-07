import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
}

tasks {
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
