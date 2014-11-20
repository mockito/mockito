package org.mockito.release.notes;

import org.gradle.api.Project;
import org.mockito.release.notes.versions.PreviousVersion;
import org.mockito.release.notes.versions.PreviousVersionFromFile;

import java.io.File;

/**
 * Release notes services
 */
class Notes {

    /**
     * Release notes build based on git and GitHub.
     *
     * @param project the Gradle project
     * @param authTokenEnvVar env variable name that holds the GitHub auth token
     */
    NotesBuilder gitHubNotesBuilder(Project project, String authTokenEnvVar) {
        return new GitNotesBuilder(project, authTokenEnvVar);
    }

    /**
     * Provides previous version information based on the release notes file
     */
    PreviousVersion previousVersion(File releaseNotesFile) {
        return new PreviousVersionFromFile(releaseNotesFile);
    }
}
