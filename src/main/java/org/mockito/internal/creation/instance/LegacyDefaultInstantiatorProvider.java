/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;

/**
 * Default for the deprecated {@link InstantiatorProvider}. If this becomes the selected provider,
 * we automatically use the default {@link org.mockito.plugins.InstantiatorProvider2}.
 *
 * <p>The only way to interact with this class is via {@link org.mockito.plugins.MockitoPlugins#getDefaultPlugin(Class) MockitoPlugins#getDefaultPlugin(InstantiatorProvider.class)}</p>
 *
 * @see org.mockito.internal.configuration.plugins.PluginRegistry#getInstantiatorProvider()
 */
public class LegacyDefaultInstantiatorProvider implements InstantiatorProvider {
    final DefaultInstantiatorProvider provider = new DefaultInstantiatorProvider();

    public Instantiator getInstantiator(MockCreationSettings<?> settings) {
        final org.mockito.creation.instance.Instantiator instantiator = provider.getInstantiator(settings);

        return new Instantiator() {
            @Override
            public <T> T newInstance(Class<T> cls) throws InstantiationException {
                try {
                    return instantiator.newInstance(cls);
                } catch (org.mockito.creation.instance.InstantiationException e) {
                    throw new InstantiationException(e.getMessage(), e.getCause());
                }
            }
        };
    }
}
