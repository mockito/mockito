/*
 * Copyright (c) 2012 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.internal.creation.MockSettingsImpl;

public interface IMockMaker {

    /**
     * Returns a new instance of {@code typeToMock} that implements the
     * interfaces of {@code extraInterfaces}. Invocations to the methods of the
     * returned instance will be delegated to {@code handler}.
     */
    <T> T createMock(Class<T> typeToMock, Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler, MockSettingsImpl settings);

    /**
     * Returns the handler for the {@code mock}, or null if {@code mock} was not
     * a mock object created by {@link #createMock}.
     */
    public MockitoInvocationHandler getHandler(Object mock);

    /**
     * Replaces the existing handler on {@code mock} with {@code newHandler}.
     */
    public void resetMock(Object mock, MockitoInvocationHandler newHandler,
            MockSettingsImpl settings);
}
