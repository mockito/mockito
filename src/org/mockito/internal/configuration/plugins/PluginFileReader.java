package org.mockito.internal.configuration.plugins;

import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.Reader;

class PluginFileReader {

    String readPluginClass(Reader reader) throws IOException {
        for(String line: IOUtil.readLines(reader)) {
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
