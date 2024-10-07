plugins {
    // doc: https://docs.gradle.org/current/userguide/kotlin_dsl.html
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradleplugin.testLogger)
    implementation(libs.gradleplugin.animalSniffer)
    implementation(libs.gradleplugin.bnd)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
