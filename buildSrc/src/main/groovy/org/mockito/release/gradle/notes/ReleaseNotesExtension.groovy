package org.mockito.release.gradle.notes

import org.gradle.api.GradleException
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.mockito.release.notes.Notes

class ReleaseNotesExtension {

    private static final Logger LOG = Logging.getLogger(ReleaseNotesExtension)

    final static String EXT_NAME = "notes"

    File notesFile;
    final String authToken = "a0a4c0f41c200f7c653323014d6a72a127764e17"; //read-only token

    private final File workDir;
    private final String version;

    ReleaseNotesExtension(File workDir, String version) {
        this.workDir = workDir
        this.version = version
    }

    //TODO SF coverage
    private void assertConfigured() {
        if (notesFile == null || !notesFile.isFile()) {
            throw new GradleException("'notesFile' must be configured and the file must be present.\n" +
                    + "Example: ${EXT_NAME}.notesFile = project.file('docs/release-notes.md')")
        }
        if (!authToken) {
            throw new GradleException("'authToken' must be configured.\n" +
                    + "Example: ${EXT_NAME}.authToken = 'secret'")
        }
    }

    //TODO SF most of the methods don't have to be on this class
    //TODO SF groovy static compilation
    String getPreviousVersion() {
        assertConfigured()
        def firstLine = notesFile.withReader { it.readLine() }
        return Notes.previousVersion(firstLine).previousVersion
    }

    String getReleaseNotes() {
        assertConfigured()
        LOG.lifecycle("Building new release notes based on {}", notesFile)
        def builder = Notes.gitHubNotesBuilder(workDir, authToken)
        def prev = "v" + getPreviousVersion()
        def current = "HEAD"
        LOG.lifecycle("Building notes for revisions: {} -> {}", prev, current)
        def newContent = builder.buildNotes(version, prev, current)
        newContent
    }

    void updateReleaseNotes() {
        def newContent = getReleaseNotes()
        def existing = notesFile.text
        notesFile.text = newContent + existing
        LOG.lifecycle("Successfully updated release notes!")
    }
}
