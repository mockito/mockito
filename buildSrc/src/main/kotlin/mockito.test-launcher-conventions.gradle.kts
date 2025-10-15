plugins {
    `jvm-toolchains`
}

tasks.withType<Test> {
    // Apply the CI test launcher configuration to any test tasks.
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = providers
            .gradleProperty("mockito.test.java")
            .map {
                if (it == "auto") {
                    JavaLanguageVersion.of(JavaVersion.current().majorVersion)
                // ErrorProne minimum supported version is 17: https://github.com/google/error-prone/issues/3803
                } else if (project.name == "mockito-errorprone" && it.toInt() < 17) {
                    JavaLanguageVersion.of(17)
                } else {
                    JavaLanguageVersion.of(it)
                }
            }
    }
}
