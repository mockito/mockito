/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.logging;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VerboseLoggingTest {

    @Test
    public void verboseLogging_should_not_log_verification_calls() throws Exception {
        // Capture both System.out and System.err (Mockito default logger prints here)
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
        try {
            // Enable verbose logging on mock
            MockSettings settings = Mockito.withSettings().verboseLogging();
            @SuppressWarnings("unchecked")
            List<String> list = Mockito.mock(List.class, settings);

            // One real invocation
            @SuppressWarnings("unused")
            int ignored = list.size();

            // One verification (should NOT produce a second verbose log)
            Mockito.verify(list).size();

            // Merge outputs
            String logs =
                    out.toString(StandardCharsets.UTF_8) + err.toString(StandardCharsets.UTF_8);

            // Count the distinct verbose header lines – there should be exactly one
            String header = "############ Logging method invocation #";
            int headers = countOccurrences(logs, header);

            assertEquals("Verification should not produce a second verbose log", 1, headers);
        } finally {
            System.setOut(oldOut);
            System.setErr(oldErr);
        }
    }

    private static int countOccurrences(String haystack, String needle) {
        int n = 0, i = 0;
        while ((i = haystack.indexOf(needle, i)) != -1) {
            n++;
            i += needle.length();
        }
        return n;
    }
}
