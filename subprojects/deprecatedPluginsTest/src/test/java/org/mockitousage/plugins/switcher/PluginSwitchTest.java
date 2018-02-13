/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins.switcher;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.exceptions.base.MockitoException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PluginSwitchTest {
    @Test
    public void uses_custom_instantiator_provider() {
        MyInstantiatorProvider.invokedFor.remove();
        mock(PluginSwitchTest.class);
        assertEquals(MyInstantiatorProvider.invokedFor.get(), asList(PluginSwitchTest.class));
    }

    @Test(expected = InstantiationException.class)
    public void exception_while_instantiating() throws Throwable {
        MyInstantiatorProvider.shouldExcept.set(true);
        try {
            mock(PluginSwitchTest.class);
        } catch (MockitoException e) {
            throw e.getCause();
        } finally {
            MyInstantiatorProvider.shouldExcept.remove();
        }
    }
}
