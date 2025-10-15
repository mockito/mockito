plugins {
    id("java")
    id("mockito.test-conventions")
}

description = "Mockito preconfigured inline mock maker (intermediate and to be superseeded by automatic usage in a future version)"

dependencies {
    implementation(project(":mockito-core"))
    testImplementation(libs.junit4)
    testImplementation(libs.assertj)
}
tasks {
    test {
        if (JavaVersion.VERSION_17 <= JavaVersion.current()) {
            // For Java 17: https://openjdk.org/jeps/403
            // > Ignoring option --illegal-access=deny; support was removed in 17.0
        } else if (JavaVersion.VERSION_16 <= JavaVersion.current()) {
            // For Java 16: https://openjdk.org/jeps/396
            // --illegal-access=deny is the default.
        } else if (JavaVersion.VERSION_1_9 <= JavaVersion.current()) {
            // For Java 9-15: https://openjdk.org/jeps/261#Relaxed-strong-encapsulation
            jvmArgs("--illegal-access=deny")
        }
        //required by the "StressTest.java" and "OneLinerStubStressTest.java"
        maxHeapSize = "256m"
    }
}
