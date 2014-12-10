package org.mockito.internal.configuration.plugins;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class PluginFileReader {

    String readPluginClass(Reader reader) throws IOException {
        List<String> lines = readerToLines(reader);
        for (String line : lines) {
            String stripped = stripCommentAndWhitespace(line);
            if (stripped.length() > 0) {
                return stripped;
            }
        }
        return null;
    }

    private static List<String> readerToLines(Reader reader) throws IOException {
        List<String> result = new ArrayList<String>();
        BufferedReader lineReader = new BufferedReader(reader);
        try {
            String line;
            while ((line = lineReader.readLine()) != null) {
                result.add(line);
            }
            return result;
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
