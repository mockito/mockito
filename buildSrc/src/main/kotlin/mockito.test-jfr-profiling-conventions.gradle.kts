plugins.withType(JavaPlugin::class) {
    tasks.withType<Test>().configureEach {
        val taskName = name
        /*
         * If the gradle option -Pjfr is set during test execution, JFR will be activated for that execution.
         */
        if (project.extensions.extraProperties.has("jfr")) {
            val jfrDir = "build/jfr"
            val jfrConfigFile = rootProject.file("config/jfr/jfr_config.jfc")
            val jfrFile = project.file("$jfrDir/${taskName}_Exec.jfr")

            jvmArgs(
                //https://docs.oracle.com/en/java/javase/17/docs/specs/man/java.html
                // Search for -XX:FlightRecorderOptions and -XX:StartFlightRecording for more details in the documentation
                "-XX:+UnlockDiagnosticVMOptions",
                "-XX:+DebugNonSafepoints",
                "-XX:FlightRecorderOptions=stackdepth=1024",
                "-XX:StartFlightRecording=name=TestExec_$taskName,disk=true,maxsize=1g,dumponexit=true,filename=$jfrFile,settings=${jfrConfigFile.absolutePath}")

            //We do not want to be UP-TO-DATE, if we are doing a Java Flight Recording. We always want a new recording.
            outputs.upToDateWhen { false }

            doFirst {
                //Ensure that the jfr folder exists, otherwise the Java process start will fail.
                project.file(jfrDir).mkdirs()
            }

            doLast{
                logger.lifecycle("Java Flight Recording was written to: $jfrFile")
            }
        }
    }
}

