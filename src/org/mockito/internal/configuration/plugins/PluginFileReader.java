package org.mockito.internal.configuration.plugins;

import java.io.IOException;
import java.io.InputStream;

import org.mockito.internal.util.io.IOUtil;

class PluginFileReader {

    String readPluginClass(final InputStream input) throws IOException {
        for(final String line: IOUtil.readLines(input)) {
            final String stripped = stripCommentAndWhitespace(line);
            if (stripped.length() > 0) {
                return stripped;
            }
        }
        return null;
    }

    private static String stripCommentAndWhitespace(String line) {
        final int hash = line.indexOf('#');
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }
}
