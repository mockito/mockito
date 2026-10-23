/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class JupiterOsgiTest {
    @Rule public TemporaryFolder storage = TemporaryFolder.builder().assureDeletion().build();

    @Test
    public void runs_jupiter_tests_from_bundles() throws Throwable {
        assertNull(JupiterOsgiTest.class.getClassLoader().getResource("org/mockito/Mockito.class"));
        assertNull(
                JupiterOsgiTest.class
                        .getClassLoader()
                        .getResource("org/junit/jupiter/api/Test.class"));

        Map<String, String> configuration =
                Map.of(
                        Constants.FRAMEWORK_STORAGE, storage.getRoot().getAbsolutePath(),
                        Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "sun.misc,sun.reflect",
                        Constants.FRAMEWORK_BOOTDELEGATION,
                                "org.mockito.internal.creation.bytebuddy.inject");
        Framework framework =
                ServiceLoader.load(FrameworkFactory.class)
                        .iterator()
                        .next()
                        .newFramework(configuration);
        try {
            framework.init();
            Bundle testBundle = null;
            for (String path :
                    System.getProperty("testRuntimeBundles")
                            .split(Pattern.quote(File.pathSeparator))) {
                Bundle installed =
                        framework
                                .getBundleContext()
                                .installBundle(new File(path).toURI().toString());
                if ("mockito.junit.compatibility.tests".equals(installed.getSymbolicName())) {
                    testBundle = installed;
                }
            }
            assertNotNull("Test bundle was not installed", testBundle);
            framework.start();
            testBundle.start();
            testBundle
                    .loadClass("org.mockitousage.osgi.JupiterOsgiTest")
                    .getMethod("execute")
                    .invoke(null);
        } catch (InvocationTargetException failure) {
            throw failure.getCause();
        } finally {
            framework.stop();
            framework.waitForStop(10000);
        }
    }
}
