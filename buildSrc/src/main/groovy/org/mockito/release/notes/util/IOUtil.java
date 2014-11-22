package org.mockito.release.notes.util;

import java.io.*;

//TODO SF document and cover
public class IOUtil {

    public static String readStream(InputStream is) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(is));
            return readNow(is);
        } catch (Exception e) {
            throw new RuntimeException("Problems reading stream", e);
        } finally {
            close(r);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("Problems closing stream", e);
            }
        }
    }

    private static String readNow(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        while(null != (line = r.readLine())) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }
}
