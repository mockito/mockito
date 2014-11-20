package org.mockito.release.notes.versions;

import java.io.File;

/**
 * Version services
 */
public class Versions {

    /**
     * Provides a way to retrieve previous version based on the content of the release notes file.
     */
    PreviousVersion previousFromNotesFile(File releaseNotesFile) {
        return new PreviousVersionFromFile(releaseNotesFile);
    }
}
