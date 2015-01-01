package org.mockito.release.version;

import org.mockito.release.notes.util.IOUtil;

import java.io.File;

class DefaultVersionTool implements VersionTool {

    private final VersionBumper bumper;

    DefaultVersionTool(VersionBumper bumper) {
        this.bumper = bumper;
    }

    public String incrementVersion(String currentVersion, File target) {
        String inc = bumper.incrementVersion(currentVersion);
        String content = IOUtil.readFully(target);
        String updated = content.replaceAll("(?s)version=(.*?)\n", "version=" + inc + "\n");
        IOUtil.writeFile(target, updated);
        return inc;
    }
}
