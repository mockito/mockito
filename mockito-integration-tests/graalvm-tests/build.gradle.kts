plugins {
    id("java")
    id("mockito.test-conventions")
    id("org.graalvm.buildtools.native") version "0.11.1"
}

description = "Test suite for exercising subclass mock maker with GraalVM native image"

dependencies {
    implementation(project(":mockito-core"))
    implementation(project(":mockito-extensions:mockito-subclass"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
    testImplementation(libs.junit.platform.launcher)
}

// org.graalvm.buildtools.native:0.11.0 requires Java >=17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

graalvmNative {
    binaries {
        named("test") {
            buildArgs.addAll("--verbose", "-O0")
        }
    }

    agent {
        enabled = project.hasProperty("enableGraalTracingAgent")
        defaultMode = "standard"

        metadataCopy {
            inputTaskNames.add("test")
            outputDirectories.add("src/test/resources/META-INF/native-image/org.mockito/graalvm-tests")
            mergeWithExisting = false
        }
    }
}

tasks {
    test {
        forkEvery = 1
        if (project.hasProperty("enableGraalTracingAgent")) {
            finalizedBy("metadataCopy")
        }
    }
}
