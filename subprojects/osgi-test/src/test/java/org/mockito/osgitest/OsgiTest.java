/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;
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
import org.junit.AfterClass;

import static org.junit.Assert.fail;

@RunWith(OsgiTest.class)
public class OsgiTest extends Suite {

    private static final FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
    private static final String STORAGE_TEMPDIR_NAME = "osgi-test-storage";
    private static final List<String> EXTRA_SYSTEMPACKAGES = Arrays.asList("org.junit", "sun.misc", "sun.reflect");

    private static final List<Path> TEST_RUNTIME_BUNDLES = splitPaths(System.getProperty("testRuntimeBundles"));
    private static final String TEST_BUNDLE_SYMBOLIC_NAME = "testBundle";

    private static final long STOP_TIMEOUT_MS = 10000;

    private static Path frameworkStorage;
    private static Framework framework;
    private static Bundle testBundle;

    public OsgiTest(Class<?> osgiTestClass, RunnerBuilder builder) throws Exception {
        super(builder, osgiTestClass, setUpClasses());
    }

    private static Class<?>[] setUpClasses() throws Exception {
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
        try {
            // Manual start to get a better exception if the bundle cannot be resolved
            testBundle.start();
        } catch (BundleException e) {
            throw new IllegalStateException("Failed to start test bundle.", e);
        }
        return getTestClasses();
    }

    private static Class<?>[] getTestClasses() throws Exception {
        return new Class<?>[] {
            loadTestClass("SimpleMockTest"),
            loadTestClass("MockNonPublicClassFailsTest"),
            loadTestClass("MockClassInOtherBundleTest")
        };
    }

    @AfterClass
    public static void tearDown() throws Exception {
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

    private static Class<?> loadTestClass(String className) throws Exception {
        return testBundle.loadClass("org.mockito.osgitest.testbundle." + className);
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
