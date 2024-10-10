plugins {
    `java-library`
    id("mockito.java-conventions")
    id("mockito.test-conventions")
    id("mockito.test-retry-conventions")
    id("mockito.test-launcher-conventions")
}

afterEvaluate {
    if (!base.archivesName.get().startsWith("mockito-")) {
        // TODO throw GradleException("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        logger.warn("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        base.archivesName = "mockito-" + project.name
    }
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.stylesheet = resources.text.fromFile("$rootDir/config/checkstyle/checkstyle.xsl")
    }
}
