/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.creation.instance.InstantiationException;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.plugins.InstantiatorProvider;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DeprecatedInstantiatorProviderTest {

    @Test
    public void provides_default_instance_for_deprecated_plugin() {
        InstantiatorProvider plugin = Mockito.framework().getPlugins().getDefaultPlugin(InstantiatorProvider.class);
        assertNotNull(plugin);
    }

    @SuppressWarnings("CheckReturnValue")
    @Test
    public void uses_custom_instantiator_provider() {
        MyDeprecatedInstantiatorProvider.invokedFor.remove();
        mock(DeprecatedInstantiatorProviderTest.class);
        assertEquals(MyDeprecatedInstantiatorProvider.invokedFor.get(), asList(DeprecatedInstantiatorProviderTest.class));
    }

    @SuppressWarnings("CheckReturnValue")
    @Test(expected = InstantiationException.class)
    public void exception_while_instantiating() throws Throwable {
        MyDeprecatedInstantiatorProvider.shouldExcept.set(true);
        try {
            mock(DeprecatedInstantiatorProviderTest.class);
        } catch (MockitoException e) {
            throw e.getCause();
        } finally {
            MyDeprecatedInstantiatorProvider.shouldExcept.remove();
        }
    }
}
