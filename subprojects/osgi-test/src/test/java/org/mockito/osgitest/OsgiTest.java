/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest;

import org.junit.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.fail;

public class OsgiTest {

    private static final FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
    private static final String STORAGE_TEMPDIR_NAME = "osgi-test-storage";
    private static final List<String> EXTRA_SYSTEMPACKAGES = Arrays.asList("sun.misc", "sun.reflect");

    private static final List<Path> TEST_RUNTIME_BUNDLES = splitPaths(System.getProperty("testRuntimeBundles"));
    private static final String TEST_BUNDLE_SYMBOLIC_NAME = "testBundle";

    private static final long STOP_TIMEOUT_MS = 10000;

    private static Path frameworkStorage;
    private static Framework framework;
    private static Bundle testBundle;

    @BeforeClass
    public static void setUp() throws IOException, BundleException {
        frameworkStorage = Files.createTempDirectory(STORAGE_TEMPDIR_NAME);
        Map<String, String> configuration = new HashMap<>();
        configuration.put(Constants.FRAMEWORK_STORAGE, frameworkStorage.toString());
        configuration.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, String.join(",", EXTRA_SYSTEMPACKAGES));
        framework = frameworkFactory.newFramework(configuration);
        framework.init();
        BundleContext bundleContext = framework.getBundleContext();
        for (Path dependencyPath : TEST_RUNTIME_BUNDLES) {
            Bundle installedBundle;
            try {
                installedBundle = bundleContext.installBundle(dependencyPath.toUri().toString());
            } catch (BundleException e) {
                throw new IllegalStateException("Failed to install bundle: " + dependencyPath.getFileName(), e);
            }
            if (TEST_BUNDLE_SYMBOLIC_NAME.equals(installedBundle.getSymbolicName())) {
                testBundle = installedBundle;
            }
        }
        if (testBundle == null) {
            fail("Test bundle not found.");
        }
        framework.start();
    }

    @AfterClass
    public static void tearDown() throws IOException, BundleException, InterruptedException {
        try {
            if (framework != null) {
                framework.stop();
                framework.waitForStop(STOP_TIMEOUT_MS);
            }
        } finally {
            if (frameworkStorage != null) {
                deleteRecursively(frameworkStorage);
            }
        }
    }

    @Test
    public void testSimpleMock() throws BundleException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        createTestRunnable("SimpleMockTest").run();
    }

    @Test
    public void testMockNonPublicClassFails() throws BundleException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        createTestRunnable("MockNonPublicClassFailsTest").run();
    }

    @Test
    public void testMockClassInOtherBundle() throws BundleException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        createTestRunnable("MockClassInOtherBundleTest").run();
    }

    private Runnable createTestRunnable(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Runnable) testBundle.loadClass("org.mockito.osgitest.testbundle." + className).newInstance();
    }

    private static List<Path> splitPaths(String paths) {
        return Stream.of(paths.split(Pattern.quote(File.pathSeparator)))
            .map(p -> Paths.get(p))
            .collect(Collectors.toList());
    }

    private static void deleteRecursively(Path pathToDelete) throws IOException {
        Files.walkFileTree(pathToDelete,
            new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
    }
}
