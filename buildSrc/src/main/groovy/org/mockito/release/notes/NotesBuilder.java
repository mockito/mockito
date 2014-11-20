package org.mockito.release.notes;

public interface NotesBuilder {

    /**
     * Release notes text for contributions between given versions.
     *
     * @param fromRevision valid git revision (can be tag name or HEAD)
     * @param toRevision valid git revision (can be tag name or HEAD)
     */
    String buildNotes(String fromRevision, String toRevision);
}