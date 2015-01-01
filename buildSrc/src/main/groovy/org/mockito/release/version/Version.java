package org.mockito.release.version;

/**
 * Version utilities
 */
public class Version {

    /**
     * Provides version tool
     */
    public static VersionTool getVersionTool() {
        return new DefaultVersionTool();
    }
}
