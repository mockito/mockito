/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.configuration.plugins.Plugins;

public class ThrowsExceptionForClassType extends AbstractThrowsException implements Serializable {

    private final Class<? extends Throwable> throwableClass;

    public ThrowsExceptionForClassType(Class<? extends Throwable> throwableClass) {
        this.throwableClass = throwableClass;
    }

    @Override
    protected Throwable getThrowable() {
        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(null);
        return instantiator.newInstance(throwableClass);
    }
}
