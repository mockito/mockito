plugins {
    `java-library`
    id("mockito.test-conventions")
}

afterEvaluate {
    if (!base.archivesName.get().startsWith("mockito-")) {
        // TODO throw GradleException("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        logger.warn("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        base.archivesName = "mockito-" + project.name
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.stylesheet = resources.text.fromFile("$rootDir/config/checkstyle/checkstyle.xsl")
    }
}
