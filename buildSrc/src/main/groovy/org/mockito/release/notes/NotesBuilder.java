package org.mockito.release.notes;

import java.util.Map;

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
     * @param labels GitHub labels to caption mapping
     *  TODO SF the labels better, currently they are coupled way too much with more generic interfaces, vcs agnostic
     */
    String buildNotes(String version, String fromRevision, String toRevision, Map<String, String> labels);
}