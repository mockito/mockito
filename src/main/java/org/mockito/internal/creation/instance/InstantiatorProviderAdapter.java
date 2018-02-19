/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;

/**
 * Adapts old, deprecated {@link InstantiatorProvider} onto a new public {@link InstantiatorProvider2} API.
 */
public class InstantiatorProviderAdapter implements InstantiatorProvider2 {
    private final InstantiatorProvider provider;

    public InstantiatorProviderAdapter(InstantiatorProvider provider) {
        this.provider = provider;
    }

    @Override
    public Instantiator getInstantiator(final MockCreationSettings<?> settings) {
        return new Instantiator() {
            @Override
            public <T> T newInstance(Class<T> cls) throws InstantiationException {
                try {
                    return provider.getInstantiator(settings).newInstance(cls);
                } catch (org.mockito.internal.creation.instance.InstantiationException e) {
                    throw new InstantiationException(e.getMessage(), e.getCause());
                }
            }
        };
    }
}
