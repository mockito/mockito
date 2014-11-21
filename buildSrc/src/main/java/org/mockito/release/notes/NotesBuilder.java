package org.mockito.release.notes;

/**
 * Builds the release notes text
 */
public interface NotesBuilder {

    /**
     * Release notes text for contributions between given versions.
     *
     * @param version the version of the release we're building the notes
     * @param fromRevision valid git revision (can be tag name or HEAD)
     * @param toRevision valid git revision (can be tag name or HEAD)
     */
    String buildNotes(String version, String fromRevision, String toRevision);
}