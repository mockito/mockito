/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Proxy;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.plugins.InternalPlugins;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;
import org.mockitoutil.TestBase;

public class MockitoPluginsTest extends TestBase {

    private final MockitoPlugins plugins = Mockito.framework().getPlugins();

    @Test
    public void provides_built_in_plugins() {
        assertNotNull(plugins.getInlineMockMaker());
        assertNotNull(plugins.getDefaultPlugin(MockMaker.class));
        assertNotNull(plugins.getDefaultPlugin(StackTraceCleanerProvider.class));
        assertNotNull(plugins.getDefaultPlugin(PluginSwitch.class));
        assertNotNull(plugins.getDefaultPlugin(InstantiatorProvider2.class));
        assertNotNull(plugins.getDefaultPlugin(AnnotationEngine.class));
        assertNotNull(plugins.getDefaultPlugin(MockitoLogger.class));
    }

    @Test
    public void provides_internal_plugins() {
        MockCandidateFilter mockCandidateFilter = InternalPlugins.getMockCandidateFilter();
        assertNotNull(mockCandidateFilter);
        // ensure PluginLoader did not create a throwing proxy instance
        assertFalse(Proxy.isProxyClass(mockCandidateFilter.getClass()));
    }
}
