package org.mockito.release.notes.versions;

import org.mockito.release.notes.util.ReleaseNotesException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Retrieves previously released version based on the top entry in the current release notes content.
 */
class FromNotesContent implements PreviousVersion {

    private final String releaseNotesContent;

    FromNotesContent(String releaseNotesContent) {
        this.releaseNotesContent = releaseNotesContent;
    }

    public String getPreviousVersion() {
        //Example: "### 1.9.5 (06-10-2012)", we want to extract "1.9.5"
        Pattern p = Pattern.compile("(?s)^### (.+?) .*");
        Matcher m = p.matcher(releaseNotesContent);
        if(!m.matches()) {
            throw new ReleaseNotesException("Unable to parse previous version from release notes content: " + releaseNotesContent, null);
        }
        return m.group(1);
    }
}