package org.mockito.release.version;

import org.mockito.release.notes.util.IOUtil;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

class DefaultVersionFile implements VersionFile {

    private final File versionFile;
    private String version;

    DefaultVersionFile(File versionFile) {
        this.versionFile = versionFile;
        this.version = readVersion(versionFile);
        if (version == null) {
            throw new IllegalArgumentException("Missing 'version=' property in file: " + versionFile);
        }
    }

    private static String readVersion(File versionFile) {
        Properties p = new Properties();
        FileReader reader = null;
        try {
            reader = new FileReader(versionFile);
            p.load(reader);
        } catch (Exception e) {
            throw new RuntimeException("Problems reading version file: " + versionFile);
        } finally {
            IOUtil.close(reader);
        }
        return p.getProperty("version");
    }

    public String getVersion() {
        return version;
    }

    public String incrementVersion() {
        VersionBumper bumper = new VersionBumper();
        version = bumper.incrementVersion(this.version);
        String content = IOUtil.readFully(versionFile);
        String updated = content.replaceAll("(?m)^version=(.*?)\n", "version=" + version + "\n");
        IOUtil.writeFile(versionFile, updated);
        return version;
    }
}
