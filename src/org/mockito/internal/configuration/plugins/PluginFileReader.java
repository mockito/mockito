package org.mockito.internal.configuration.plugins;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

class PluginFileReader {

    String readPluginClass(Reader reader) throws IOException {
        BufferedReader lineReader = new BufferedReader(reader);
        try {
            String line;
            while ((line = lineReader.readLine()) != null) {
                String stripped = stripCommentAndWhitespace(line);
                if (stripped.length() > 0) {
                    return stripped;
                }
            }
            return null;
        } finally {
            closeQuietly(lineReader);
        }
    }

    private static String stripCommentAndWhitespace(String line) {
        int hash = line.indexOf('#');
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }

    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignored) {
            }
        }
    }
}
