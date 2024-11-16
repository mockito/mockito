plugins {
    id("mockito.library-conventions")
}

description = "ErrorProne plugins for Mockito"

dependencies {
    compileOnly(libs.autoservice)
    annotationProcessor(libs.autoservice)

    implementation(project(":mockito-core"))
    implementation(libs.errorprone)

    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.errorprone.test.api)
}

tasks {
    test {
        inputs.files(configurations.errorproneJavac).withNormalizer(ClasspathNormalizer::class)
        jvmArgumentProviders.add(
            CommandLineArgumentProvider {
                listOf(
                    "-Xbootclasspath/a:${configurations.errorproneJavac.get().asPath}",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.type=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
                    "--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
                )
            }
        )
    }

    withType<JavaCompile> {
        options.compilerArgs.addAll(
            listOf(
                "--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
                "--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
                "--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
            )
        )
    }

    withType<Javadoc> {
        javadocDocletOptions {
            addBooleanOption(
                "-add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
                true
            )
        }
    }
}
