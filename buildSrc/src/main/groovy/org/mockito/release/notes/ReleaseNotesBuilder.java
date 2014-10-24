package org.mockito.release.notes;

import java.io.File;

public interface ReleaseNotesBuilder {

    /**
     * Updates release notes in specified file. The 'from' revision is extracted from the file. The 'to' revision needs to be supplied
     *
     * @param notesFile the file to update
     * @param toRevision git revision (can be tag name or HEAD)
     */
    void updateNotes(File notesFile, String toRevision);

    /**
     * Retrieves previously released version based on the release notes file.
     *
     * @param notesFile
     * @return version, for example '1.10.0'
     */
    String getPreviousVersion(File notesFile);

    /**
     * Returns contributions between two revisions. Revisions can also be tag names or HEAD, etc.
     *
     * @param fromRevision start revision
     * @param toRevision end revision
     * @return the contribution set, never null
     */
    ContributionSet getContributionsBetween(String fromRevision, String toRevision);
}