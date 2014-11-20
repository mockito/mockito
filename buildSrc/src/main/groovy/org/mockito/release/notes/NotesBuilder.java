package org.mockito.release.notes;

import java.io.File;

public interface NotesBuilder {

    /**
     * Updates release notes in specified file. The 'from' revision is extracted from the file. The 'to' revision needs to be supplied
     *
     * @param notesFile the file to update
     * @param toRevision git revision (can be tag name or HEAD)
     */
    void updateNotes(File notesFile, String toRevision);
}