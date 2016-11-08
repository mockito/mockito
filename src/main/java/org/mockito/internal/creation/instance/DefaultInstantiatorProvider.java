/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;

public class DefaultInstantiatorProvider implements InstantiatorProvider {

    private final static Instantiator INSTANCE = new ObjenesisInstantiator();

    public Instantiator getInstantiator(MockCreationSettings<?> settings) {
        if (settings != null && settings.isUsingConstructor()) {
            return new ConstructorInstantiator(settings.getOuterClassInstance());
        } else {
            return INSTANCE;
        }
    }
}