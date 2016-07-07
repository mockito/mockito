package org.mockito.release.notes.util;

import java.io.*;
import java.util.Scanner;

/**
 * IO utils. A bit of reinventing the wheel but we don't want extra dependencies at this stage and we want to be java.
 */
public class IOUtil {

    /**
     * Reads string from the file
     */
    public static String readFully(File input) {
        try {
            return readNow(new FileInputStream(input));
        } catch (Exception e) {
            throw new RuntimeException("Problems reading file: " + input, e);
        }
    }

    /**
     * Reads string from the stream and closes it
     */
    public static String readFully(InputStream stream) {
        try {
            return readNow(stream);
        } catch (Exception e) {
            throw new RuntimeException("Problems reading stream", e);
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

    private static String readNow(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        try {
            return s.hasNext() ? s.next() : "";
        } finally {
            s.close();
        }
    }

    public static void writeFile(File target, String content) {
        PrintWriter p = null;
        try {
            p = new PrintWriter(new FileWriter(target));
            p.write(content);
        } catch (Exception e) {
            throw new RuntimeException("Problems writing text to file: " + target);
        } finally {
            close(p);
        }
    }
}
