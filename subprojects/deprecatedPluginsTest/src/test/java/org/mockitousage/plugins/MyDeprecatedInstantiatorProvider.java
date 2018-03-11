/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins;

import org.mockito.Mockito;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MyDeprecatedInstantiatorProvider implements InstantiatorProvider {
    static ThreadLocal<Boolean> shouldExcept = new ThreadLocal<>();
    static ThreadLocal<List<Class>> invokedFor = new ThreadLocal<>();

    @Override
    public Instantiator getInstantiator(MockCreationSettings<?> settings) {
        if (shouldExcept.get() != null) {
            throw new InstantiationException(null, null);
        }

        if (invokedFor.get() == null) {
            invokedFor.set(new LinkedList<>());
        }
        invokedFor.get().add(settings.getTypeToMock());

        return Mockito.framework().getPlugins().getDefaultPlugin(InstantiatorProvider.class).getInstantiator(settings);
    }
}
