import ru.vyarus.gradle.plugin.animalsniffer.AnimalSnifferExtension

plugins {
    java // for sourceSets
    id("ru.vyarus.animalsniffer")
}


val main: SourceSet by sourceSets.getting
configure<AnimalSnifferExtension> {
    sourceSets = listOf(main)
    annotation = "org.mockito.internal.SuppressSignatureCheck"

    // See please https://github.com/mojohaus/animal-sniffer/issues/172
    ignore(
        "java.lang.instrument.Instrumentation",
        "java.lang.invoke.MethodHandle",
        "java.lang.invoke.MethodHandles\$Lookup",
        "java.lang.StackWalker",
        "java.lang.StackWalker\$StackFrame",
        "java.lang.StackWalker\$Option",
    )
}

dependencies {
    // Equivalent to "net.sf.androidscents.signature:android-api-level-26:8.0.0_r2@signature"
    signature(variantOf(libs.animalSniffer.android.apiLevel26) { artifactType("signature") })
    signature(variantOf(libs.animalSniffer.java) { artifactType("signature") })
}

