plugins {
    `java-library`
    id("mockito.java-conventions")
    id("mockito.test-conventions")
    id("mockito.test-retry-conventions")
    id("mockito.test-launcher-conventions")
    id("net.ltgt.errorprone")
}

if (!project.name.startsWith("mockito-")) {
    throw GradleException("Published project module should be prefixed with `mockito-` : ${project.projectDir})")
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
