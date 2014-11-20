package org.mockito.release.notes.exec;

import org.gradle.api.Project;

public class Exec {

    public static ProcessRunner getGradleProcessRunner(Project project) {
        return new GradleProcessRunner(project);
    }
}
