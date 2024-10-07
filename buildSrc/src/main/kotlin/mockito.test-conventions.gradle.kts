import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    `jvm-toolchains`
    id("com.adarshr.test-logger")
}

testlogger {
    theme = ThemeType.MOCHA_PARALLEL
    isShowPassed = false
}

tasks {
    // Configure the main "test" task for `java` only projects.
    plugins.withType(JavaPlugin::class) {
        named<Test>("test") {
            // This ignores classes with JUnit annotations not ending with "Test"
            include("**/*Test.class")

            if (JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)
                && System.getenv("MEMBER_ACCESSOR") == "member-accessor-reflection") {
                jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
            }
        }
    }

    withType<Test> {
        // Apply the CI test launcher configuration to any test tasks.
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
