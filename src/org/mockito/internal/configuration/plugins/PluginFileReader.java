package org.mockito.internal.configuration.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

class PluginFileReader {

    List<String> readerToLines(Reader reader) throws IOException {
        List<String> result = new ArrayList<String>();
        BufferedReader lineReader = new BufferedReader(reader);
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    String stripCommentAndWhitespace(String line) {
        int hash = line.indexOf('#');
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }

    void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }
}
