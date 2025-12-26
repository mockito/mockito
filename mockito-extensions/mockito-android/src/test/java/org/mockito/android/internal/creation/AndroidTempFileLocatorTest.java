package org.mockito.android.internal.creation;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

/**
 * Tests for AndroidTempFileLocator.guessPath(String).
 *
 * These tests invoke the private method via reflection. Since guessPath creates File objects
 * with absolute paths like "/data/data/<packageName>", and these directories are unlikely to
 * exist or be writable on your system, these tests expect an empty result in those cases.
 */
public class AndroidTempFileLocatorTest {
    /**
    * Helper method to invoke the private static guessPath(String) method.
    */
    private File[] invokeGuessPath(String input) throws Exception {
        Method method = AndroidTempFileLocator.class.getDeclaredMethod("guessPath", String.class);
        method.setAccessible(true);
        return (File[]) method.invoke(null, input);
    }

    @Test
    public void guessPathvalidApkPathwithRealWritableDirs() throws Exception {
        // Make sure /data/data/com.example/cache exists and is writable.
        File dataDir = new File("src/test/resources/data/data/com.example");
        File cacheDir = new File(dataDir, "cache");

        // If the directory doesn't exist in your environment, skip the test.
        assumeTrue(
            "Skipping test: /data/data/com.example/cache must exist and be writable",
            dataDir.isDirectory() && dataDir.canWrite() &&
            cacheDir.isDirectory() && cacheDir.canWrite()
        );

        // Provide a valid path that guessPath recognizes.
        String input = "/data/app/com.example-1.apk:/system/app/com.example.apk";
        File[] result = invokeGuessPath(input);

        assertNotNull("Result should not be null", result);
        // Because we've ensured the directory is there and writable,
        // guessPath should find it and return an array of length 1.
        assertEquals("Expected exactly one result if cache directory is available", 1, result.length);
        assertEquals("The returned directory should match /data/data/com.example/cache", cacheDir, result[0]);
    }

    /**
     * Test with an input that does not start with "/data/app/".
     * Expected: guessPath returns an empty array.
     */
    @Test
    public void testGuessPathInvalidPathNoDataApp() throws Exception {
        String input = "/system/app/com.example.apk";
        File[] results = invokeGuessPath(input);
        assertNotNull("Results should not be null", results);
        assertEquals("Expected no results for a path not starting with /data/app/", 0, results.length);
    }

    /**
     * Test with an input that does not properly end with ".apk".
     * Expected: guessPath returns an empty array.
     */
    @Test
    public void testGuessPathInvalidPathNoApk() throws Exception {
        String input = "/data/app/com.example.txt";
        File[] results = invokeGuessPath(input);
        assertNotNull("Results should not be null", results);
        assertEquals("Expected no results for a path that doesn't end with .apk", 0, results.length);
    }

    /**
     * Test with a valid APK path format.
     * Since the expected directory "/data/data/com.example" likely does not exist or isn’t writable,
     * guessPath should return an empty array.
     */
    @Test
    public void testGuessPathValidApkButNonExistingDirectories() throws Exception {
        String input = "/data/app/com.example-1.apk";
        File[] results = invokeGuessPath(input);
        assertNotNull("Results should not be null", results);
        assertEquals("Expected no results when data directory does not exist or is not writable", 0, results.length);
    }
}