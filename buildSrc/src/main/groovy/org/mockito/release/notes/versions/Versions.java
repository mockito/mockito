package org.mockito.release.notes.versions;

/**
 * Version services
 */
public class Versions {

    /**
     * Provides a way to retrieve previous version based on the content of the release notes file.
     */
    public static PreviousVersion previousFromNotesContent(String notesContent) {
        return new FromNotesContent(notesContent);
    }
}
