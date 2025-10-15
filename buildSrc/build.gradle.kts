plugins {
    // doc: https://docs.gradle.org/current/userguide/kotlin_dsl.html
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradleplugin.testLogger)
    implementation(libs.gradleplugin.animalSniffer)
    implementation(libs.gradleplugin.bnd)
    implementation(libs.gradleplugin.errorprone)
    implementation(libs.gradleplugin.spotless)
    implementation(libs.gradleplugin.spotless.googleJavaFormat)
    implementation(libs.gradleplugin.license)

    implementation(libs.gradleplugin.nexusPublish)
    implementation(libs.gradleplugin.shipkit.changelog)
    implementation(libs.gradleplugin.shipkit.autoVersion)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
