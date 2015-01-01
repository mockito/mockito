package org.mockito.release.version;

/**
 * Operations with versions
 */
public interface VersionTool {

    /**
     * Increments provided version and returns the result
     */
    String incrementVersion(String currentVersion);
}
