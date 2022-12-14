/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitosubclass;

import org.junit.Test;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.util.reflection.ModuleMemberAccessor;

import static org.junit.Assert.*;

public class PluginTest {

    @Test
    public void mock_maker_should_be_inline() throws Exception {
        assertTrue(Plugins.getMockMaker() instanceof ByteBuddyMockMaker);
    }

    @Test
    public void member_accessor_should_be_module() throws Exception {
        assertTrue(Plugins.getMemberAccessor() instanceof ModuleMemberAccessor);
    }

}
