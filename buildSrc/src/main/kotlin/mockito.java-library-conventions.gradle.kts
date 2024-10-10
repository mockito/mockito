plugins {
    `java-library`
    id("mockito.java-conventions")
    id("mockito.test-conventions")
    id("mockito.test-retry-conventions")
    id("mockito.test-launcher-conventions")
    id("net.ltgt.errorprone")
}

afterEvaluate {
    if (!base.archivesName.get().startsWith("mockito-")) {
        // TODO throw GradleException("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        logger.warn("Project module should be prefixed with `mockito-` : ${project.projectDir})")
        base.archivesName = "mockito-" + project.name
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    errorprone(libs.errorprone)
}

tasks {
    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        // dirMode = Integer.parseInt("0755", 8)
        dirPermissions {
            unix("rwxr-xr-x") // 0755
        }
        // fileMode = Integer.parseInt("0644", 8)
        filePermissions {
            unix("rw-r--r--") // 0644
        }
    }
}
