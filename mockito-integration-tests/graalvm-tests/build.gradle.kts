plugins {
    id("java")
    id("mockito.test-conventions")
    id("org.graalvm.buildtools.native") version "0.11.0"
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
        // Only enable the tracing agent when the JVM vendor is GraalVM
        enabled = System.getProperty("java.vendor", "").contains("GraalVM")
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
    }

    register("generateMetadata") {
        group = "graalvm"
        description = "Generate GraalVM metadata files by running tests with agent"
        dependsOn("test")
        finalizedBy("metadataCopy")
    }

    register("nativeTestWithMetadata") {
        group = "graalvm"
        description = "Run native tests using generated metadata"
        dependsOn("generateMetadata")
        finalizedBy("nativeTest")
    }
}
