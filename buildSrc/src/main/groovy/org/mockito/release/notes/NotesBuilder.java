package org.mockito.release.notes;

public interface NotesBuilder {

//    /**
//     * Updates release notes in specified file. The 'from' revision is extracted from the release notes file.
//     * The 'to' revision needs to be supplied.
//     *
//     * @param notesFile the file to update. Must contain the current version.
//     * @param toRevision valid git revision (can be tag name or HEAD)
//     */
//    void updateNotes(File notesFile, String toRevision);

    /**
     * Release notes text for contributions between given versions.
     *
     * @param fromRevision valid git revision (can be tag name or HEAD)
     * @param toRevision valid git revision (can be tag name or HEAD)
     */
    String getNotes(String fromRevision, String toRevision);
}