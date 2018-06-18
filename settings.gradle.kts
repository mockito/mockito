include("deprecatedPluginsTest",
    "inline",
    "testng",
    "extTest",
    "kotlinTest",
    "android",
    "junit-jupiter",
    "junitJupiterExtensionTest")

rootProject.name = "mockito"

rootProject.children.forEach { project ->
    val projectDirName = "subprojects/${project.name}"
    project.projectDir = File(settingsDir, projectDirName)
    project.buildFileName = "${project.name}.gradle"
    assert(project.projectDir.isDirectory)
    assert(project.buildFile.isFile)
}
