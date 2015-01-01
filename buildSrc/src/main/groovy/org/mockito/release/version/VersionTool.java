package org.mockito.release.version;

import java.io.File;

/**
 * Operations with versions
 */
public interface VersionTool {

    /**
     * Increments provided version, writes it to the target file and returns the incremented version
     */
    String incrementVersion(String currentVersion, File target);
}
