import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("mockito.java-conventions")
    id("com.adarshr.test-logger")
    id("mockito.quality-spotless-conventions")
    id("mockito.license-conventions")
}

repositories {
    mavenCentral()
    google()
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
                && providers.environmentVariable("MEMBER_ACCESSOR").orNull == "member-accessor-reflection") {
                jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
            }
        }
    }
}
