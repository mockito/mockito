package org.mockito.release.version;

/**
 * The file that contains version number
 */
public interface VersionFile {

    /**
     * the version number
     */
    String getVersion();

    /**
     * increments version number in the file and returns incremented value
     */
    String incrementVersion();

}
