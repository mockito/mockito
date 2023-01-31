plugins {
  id("com.gradle.enterprise").version("3.3.4")
}

include("subclass",
    "inline",
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
    "programmatic-test")

// https://developer.android.com/studio/command-line/variables#envar
// https://developer.android.com/studio/build#properties-files
if (System.getenv("ANDROID_HOME") != null || File("local.properties").exists()) {
    include("androidTest")
} else {
    logger.info("Not including android test project due to missing SDK configuration")
}

rootProject.name = "mockito"

val koltinBuildScriptProject = hashSetOf("junitJupiterExtensionTest", "junitJupiterInlineMockMakerExtensionTest")

fun buildFileExtensionFor(projectName: String) =
    if (projectName in koltinBuildScriptProject) ".gradle.kts" else ".gradle"

fun buildFileFor(projectName: String) =
    "$projectName${buildFileExtensionFor(projectName)}"

rootProject.children.forEach { project ->
    val projectDirName = "subprojects/${project.name}"
    project.projectDir = File(settingsDir, projectDirName)
    project.buildFileName = buildFileFor(project.name)
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
