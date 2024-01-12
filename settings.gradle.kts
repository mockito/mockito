plugins {
    id("com.gradle.enterprise") version "3.16.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(
    "subclass",
    "inlineTest",
    "proxy",
    "extTest",
    "groovyTest",
    "groovyInlineTest",
    "kotlinTest",
    "kotlinReleaseCoroutinesTest",
    "android",
    "junit-jupiter",
    "junitJupiterExtensionTest",
    "junitJupiterInlineMockMakerExtensionTest",
    "module-test",
    "memory-test",
    "junitJupiterParallelTest",
    "osgi-test",
    "bom",
    "errorprone",
    "programmatic-test",
    "java21"
)

// https://developer.android.com/studio/command-line/variables#envar
// https://developer.android.com/studio/build#properties-files
if (System.getenv("ANDROID_HOME") != null || File("local.properties").exists()) {
    include("androidTest")
} else {
    logger.info("Not including android test project due to missing SDK configuration")
}

rootProject.name = "mockito"

rootProject.children.forEach { project ->
    val projectDirName = "subprojects/${project.name}"
    project.projectDir = File(settingsDir, projectDirName)
    project.buildFileName = "${project.name}.gradle"
    require(project.projectDir.isDirectory) {
        "Project directory ${project.projectDir} for project ${project.name} does not exist."
    }
    require(project.buildFile.isFile) {
        "Build file ${project.buildFile} for project ${project.name} does not exist."
    }
}

//Posting Build scans to https://scans.gradle.com
gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

buildCache {
    local {
        isEnabled = !System.getenv().containsKey("CI")
    }
}
