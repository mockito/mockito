/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.instantiator;

import org.mockito.creation.instance.InstantiationException;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.creation.instance.DefaultInstantiatorProvider;
import org.mockito.mock.MockCreationSettings;

public class MyInstantiatorProvider2 extends DefaultInstantiatorProvider {
    static ThreadLocal<Boolean> explosive = new ThreadLocal<>();

    @Override
    public Instantiator getInstantiator(MockCreationSettings<?> settings) {
        if (explosive.get() != null) {
            throw new InstantiationException(MyInstantiatorProvider2.class.getName(), null);
        }
        return super.getInstantiator(settings);
    }
}
