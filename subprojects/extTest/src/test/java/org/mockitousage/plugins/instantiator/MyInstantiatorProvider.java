/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.instantiator;

import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;

@SuppressWarnings("deprecation")
public class MyInstantiatorProvider implements InstantiatorProvider {
    @Override
    public Instantiator getInstantiator(MockCreationSettings<?> settings) {
        throw new RuntimeException("Should never be called as there is InstantiatorProvider2 plugin");
    }
}
