include("deprecatedPluginsTest",
    "inline",
    "extTest",
    "kotlinTest",
    "kotlinReleaseCoroutinesTest",
    "android",
    "junit-jupiter",
    "junitJupiterExtensionTest",
    "module-test",
    "memory-test",
    "errorprone")

rootProject.name = "mockito"

val koltinBuildScriptProject = hashSetOf("junitJupiterExtensionTest")

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

buildCache {
    local {
        isEnabled = !System.getenv().containsKey("CI")
    }
}
