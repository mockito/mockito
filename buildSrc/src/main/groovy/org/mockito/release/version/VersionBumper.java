package org.mockito.release.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VersionBumper {

    /**
     * Increments 'patch' element of the version of provided version, e.g. 1.0.0 -> 1.0.1
     */
    String incrementVersion(String version) {
        Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(-(\\w+)){0,1}");
        Matcher matcher = pattern.matcher(version);
        boolean m = matcher.matches();
        if (!m) {
            throw new IllegalArgumentException("Unsupported version: '" + version + "'. Examples of supported versions: 1.0.0, 1.20.123, 1.0.10-beta");
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));
        String postfix = matcher.group(4) != null ? matcher.group(4) : "";
        return "" + major + "." + minor + "." + (patch + 1) + postfix;
    }
}
