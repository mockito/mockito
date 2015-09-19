package org.mockito.internal.configuration.plugins;

import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

class PluginFileReader {

    String readPluginClass(InputStream input) throws IOException {
        for(String line: IOUtil.readLines(input)) {
            String stripped = stripCommentAndWhitespace(line);
            if (stripped.length() > 0) {
                return stripped;
            }
        }
        return null;
    }

    private static String stripCommentAndWhitespace(String line) {
        int hash = line.indexOf('#');
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }
}
