package org.mockito.release.notes.exec;

import org.gradle.api.Project
import org.gradle.process.ExecSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

class GradleProcessRunner implements ProcessRunner {

    private static Logger LOG = LoggerFactory.getLogger(GradleProcessRunner.class);

    private final Project project;

    GradleProcessRunner(Project project) {
        this.project = project;
    }

    public String run(String... commandLine) {
        LOG.info("Executing command: " + Arrays.toString(commandLine));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //TODO SF we can turn this class into java once we're using newer version of Gradle
        //which has exec method that receives an Action
        project.exec { ExecSpec spec ->
            spec.commandLine(commandLine)
            spec.standardOutput = out
            spec.errorOutput = out
        }
        return out.toString();
    }
}
