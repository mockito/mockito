package org.mockito.release.notes;

import org.mockito.release.notes.versions.PreviousVersion;
import org.mockito.release.notes.versions.Versions;

import java.io.File;

/**
 * Release notes services
 */
public class Notes {

    /**
     * Release notes build based on git and GitHub.
     *
     * @param workDir working directory for executing external processes like 'git log'
     * @param authTokenEnvVar env variable name that holds the GitHub auth token
     */
    public static NotesBuilder gitHubNotesBuilder(File workDir, String authTokenEnvVar) {
        return new GitNotesBuilder(workDir, authTokenEnvVar);
    }

    /**
     * Provides previous version information based on the release notes content file
     */
    public static PreviousVersion previousVersion(String releaseNotesContent) {
        return Versions.previousFromNotesContent(releaseNotesContent);
    }
}
