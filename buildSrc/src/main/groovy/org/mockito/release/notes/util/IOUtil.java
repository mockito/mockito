package org.mockito.release.notes.util;

import java.io.*;

/**
 * IO utils. A bit of reinventing the wheel but we don't want extra dependencies at this stage and we want to be java.
 */
public class IOUtil {

    /**
     * Reads string from the stream and closes it
     */
    public static String readFully(InputStream stream) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(stream));
            return readNow(stream);
        } catch (Exception e) {
            throw new RuntimeException("Problems reading stream", e);
        } finally {
            close(r);
        }
    }

    /**
     * Closes the target. Does nothing when target is null. Is not silent, throws exception on IOException.
     *
     * @param closeable the target, may be null
     */
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
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
