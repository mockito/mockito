/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.plugins.MockMaker;

class AnyClass<T> implements LambdaArgumentMatcher<T> {

    private static final MockMaker MOCK_MAKER = Plugins.getMockMaker();

    private final Class<T> clazz;

    AnyClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T constructObject() {
        return MOCK_MAKER.createMock(new MockSettingsImpl<T>().setTypeToMock(clazz), null);
    }

    @Override
    public boolean matches(T argument) {
        return clazz.isAssignableFrom(argument.getClass());
    }
}
