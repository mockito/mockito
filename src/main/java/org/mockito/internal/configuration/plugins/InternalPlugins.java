/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

/**
 * Plugins that can hook into <code>mockito-core</code> but its interfaces are not well suited to be
 * exposed as public API.
 */
public final class InternalPlugins {

    private InternalPlugins() {}

    private static final PluginRegistry pluginRegistry = Plugins.getRegistry();

    /**
     * Returns the {@link MockCandidateFilter} available for the current runtime.
     * @return {@link org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter} if no
     *         {@link MockCandidateFilter} (internal) extension exists or is visible in the current classpath.
     */
    public static MockCandidateFilter getMockCandidateFilter() {
        return pluginRegistry.getMockCandidateFilter();
    }
}
