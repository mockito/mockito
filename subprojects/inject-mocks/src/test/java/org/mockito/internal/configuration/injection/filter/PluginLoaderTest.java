/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.junit.jupiter.api.Test;
import org.mockito.internal.configuration.plugins.InternalPlugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PluginLoaderTest {

    @Test
    public void testPluginLoads() {
        MockCandidateFilter chosenMockCandidateFilter = InternalPlugins.getMockCandidateFilter();
        assertNotNull(chosenMockCandidateFilter);
        assertEquals(TypeWithGenericsCandidateFilter.class, chosenMockCandidateFilter.getClass());
    }
}
