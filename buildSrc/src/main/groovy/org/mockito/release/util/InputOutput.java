package org.mockito.release.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utility for input/output
 */
public class InputOutput {

    /**
     * Closes input, does nothing when input is null
     */
    public static void closeStream(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                throw new RuntimeException("Problems closing stream.", e);
            }
        }
    }
}
