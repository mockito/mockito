plugins {
    id("java-platform")
    id("mockito.publication-conventions")
}

description = "Mockito Bill of Materials (BOM)"

if (!base.archivesName.get().startsWith("mockito-")) {
    logger.warn("Project module should be prefixed with `mockito-` : ${project.projectDir})")
    base.archivesName = "mockito-" + project.name
}

val mockitoExtensions: Set<Project> = project(":mockito-extensions").subprojects
dependencies {
    constraints {
        api(project(":mockito-core"))

        mockitoExtensions.forEach {
            api(it)
        }
    }
}
