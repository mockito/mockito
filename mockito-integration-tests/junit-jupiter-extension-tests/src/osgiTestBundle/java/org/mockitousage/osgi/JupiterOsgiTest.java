/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.osgi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.FrameworkUtil;

@ExtendWith(MockitoExtension.class)
public class JupiterOsgiTest {
    @Mock private List<String> fieldMock;

    @Test
    void injects_field_mock() {
        assertNotNull(fieldMock);
        assertTrue(mockingDetails(fieldMock).isMock());
    }

    @Test
    void injects_parameter_mock(@Mock List<String> parameterMock) {
        assertNotNull(parameterMock);
        assertTrue(mockingDetails(parameterMock).isMock());
    }

    @Test
    void rejects_unused_stubbing() {
        when(fieldMock.size()).thenReturn(42);
    }

    public static void execute() {
        // All these classes must come from installed bundles, including the selected JUnit runtime.
        assertNotNull(FrameworkUtil.getBundle(JupiterOsgiTest.class));
        assertNotNull(FrameworkUtil.getBundle(Mockito.class));
        assertNotNull(FrameworkUtil.getBundle(MockitoExtension.class));
        assertNotNull(FrameworkUtil.getBundle(JupiterTestEngine.class));
        assertEquals(
                System.getProperty("junitVersion"),
                FrameworkUtil.getBundle(Test.class).getVersion().toString());

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherConfig config =
                LauncherConfig.builder()
                        .enableTestEngineAutoRegistration(false)
                        .enableLauncherSessionListenerAutoRegistration(false)
                        .enableLauncherDiscoveryListenerAutoRegistration(false)
                        .enableTestExecutionListenerAutoRegistration(false)
                        .enablePostDiscoveryFilterAutoRegistration(false)
                        .addTestEngines(new JupiterTestEngine())
                        .build();
        LauncherFactory.create(config)
                .execute(
                        LauncherDiscoveryRequestBuilder.request()
                                .selectors(selectClass(JupiterOsgiTest.class))
                                .build(),
                        listener);

        StringWriter failures = new StringWriter();
        listener.getSummary().printFailuresTo(new PrintWriter(failures));
        assertEquals(3, listener.getSummary().getTestsStartedCount(), failures.toString());
        assertEquals(2, listener.getSummary().getTestsSucceededCount(), failures.toString());
        assertEquals(1, listener.getSummary().getTestsFailedCount(), failures.toString());
        assertEquals(
                UnnecessaryStubbingException.class,
                listener.getSummary().getFailures().get(0).getException().getClass(),
                failures.toString());
    }
}
