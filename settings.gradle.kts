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

buildCache {
    local {
        isEnabled = !System.getenv().containsKey("CI")
    }
}
