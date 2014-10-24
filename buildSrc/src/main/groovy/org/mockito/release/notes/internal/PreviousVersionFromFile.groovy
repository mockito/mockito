package org.mockito.release.notes.internal

/**
 * Retrieves previously released version based on the top entry in the current release notes file.
 */
class PreviousVersionFromFile implements PreviousVersionProvider {

    private final File releaseNotes

    PreviousVersionFromFile(File releaseNotes) {
        this.releaseNotes = releaseNotes
    }

    String getPreviousVersion() {
        println "Attempting to figure out the previous version from the release notes file"
        return releaseNotes.withReader {
            def firstLine = it.readLine()
            assert firstLine.startsWith('###')
            //Example: "### 1.9.5 (06-10-2012)", we want to extract "1.9.5"
            def m = firstLine =~ /### (.+?) .*/
            assert m.matches()
            return m.group(1)
        }
    }
}