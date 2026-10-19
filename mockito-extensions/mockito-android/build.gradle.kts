plugins {
    id("com.android.library")
    id("mockito.publication-conventions")
}

description = "Mockito for Android"

android {
    namespace = "org.mockito.android"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        aarMetadata {
            minCompileSdk = 1
        }
    }

    publishing {
        singleVariant("release")
    }

    enableKotlin = false
}

dependencies {
    api(project(":mockito-core"))
    implementation(libs.dexmaker.mockito.inline)
}
