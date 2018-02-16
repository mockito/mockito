/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;

/**
 * Map a {@link InstantiatorProvider2} onto a {@link InstantiatorProvider}
 */
public class InstantiatorProvider2Wrapper implements InstantiatorProvider {
    private final InstantiatorProvider2 provider;

    public InstantiatorProvider2Wrapper(InstantiatorProvider2 provider) {
        this.provider = provider;
    }

    @Override
    public Instantiator getInstantiator(final MockCreationSettings<?> settings) {
        return new Instantiator() {
            @Override
            public <T> T newInstance(Class<T> cls) throws InstantiationException {
                try {
                    return provider.getInstantiator(settings).newInstance(cls);
                } catch (org.mockito.creation.instance.InstantiationException e) {
                    throw new InstantiationException(e.getMessage(), e.getCause());
                }
            }
        };
    }
}
