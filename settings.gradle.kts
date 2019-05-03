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
    assert(project.projectDir.isDirectory)
    assert(project.buildFile.isFile)
}

enableFeaturePreview("STABLE_PUBLISHING")

buildCache {
    local {
        isEnabled = !System.getenv().containsKey("CI")
    }
}
