package org.mockito.internal.util.io;

import org.mockito.exceptions.base.MockitoException;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * IO utils. A bit of reinventing the wheel but we don't want extra dependencies at this stage and we want to be java.
 */
public class IOUtil {

    /**
     * Writes text to file
     */
    public static void writeText(String text, File output) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(output));
            pw.write(text);
        } catch (Exception e) {
            throw new MockitoException("Problems writing text to file: " + output, e);
        } finally {
            close(pw);
        }
    }

    public static Collection<String> readLines(InputStream is) {
        List<String> out = new LinkedList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while((line = r.readLine()) != null) {
                out.add(line);
            }
        } catch (IOException e) {
            throw new MockitoException("Problems reading from: " + is, e);
        }
        return out;
    }

    /**
     * Closes the target. Does nothing when target is null. Is silent.
     *
     * @param closeable the target, may be null
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            close(closeable);
        } catch (MockitoException ignored) {
            //ignore, for backwards compatibility
        }
    }

    /**
     * Closes the target. Does nothing when target is null. Is not silent and exceptions are rethrown.
     *
     * @param closeable the target, may be null
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new MockitoException("Problems closing stream: " + closeable, e);
            }
        }
    }
}
