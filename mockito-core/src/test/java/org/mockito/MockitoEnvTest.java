/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assume;
import org.junit.Test;
import org.mockito.internal.configuration.plugins.DefaultMockitoPlugins;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
import org.mockito.plugins.MockMaker;

public class MockitoEnvTest {
    @Test
    public void uses_default_mock_maker_from_env() {
        final String mockMaker = System.getenv("MOCK_MAKER");
        Assume.assumeThat(mockMaker, not(nullValue()));
        Assume.assumeThat(mockMaker, endsWith("default"));

        assertThat(DefaultMockitoPlugins.getDefaultPluginClass(MockMaker.class.getName()))
                .isEqualTo(Plugins.getMockMaker().getClass().getName());
    }

    @Test
    public void uses_mock_maker_from_env() {
        final String mockMaker = System.getenv("MOCK_MAKER");
        Assume.assumeThat(mockMaker, not(nullValue()));
        Assume.assumeThat(mockMaker, not(endsWith("default")));

        assertThat(DefaultMockitoPlugins.getDefaultPluginClass(mockMaker))
                .isEqualTo(Plugins.getMockMaker().getClass().getName());
    }

    @Test
    public void uses_default_member_accessor_from_env() {
        final String memberAccessor = System.getenv("MEMBER_ACCESSOR");
        Assume.assumeThat(memberAccessor, not(nullValue()));
        Assume.assumeThat(memberAccessor, endsWith("default"));

        assertThat(DefaultMockitoPlugins.getDefaultPluginClass(MemberAccessor.class.getName()))
                .isEqualTo(Plugins.getMemberAccessor().getClass().getName());
    }

    @Test
    public void uses_member_accessor_from_env() {
        final String memberAccessor = System.getenv("MEMBER_ACCESSOR");
        Assume.assumeThat(memberAccessor, not(nullValue()));
        Assume.assumeThat(memberAccessor, not(endsWith("default")));

        assertThat(DefaultMockitoPlugins.getDefaultPluginClass(memberAccessor))
                .isEqualTo(Plugins.getMemberAccessor().getClass().getName());
    }
}
