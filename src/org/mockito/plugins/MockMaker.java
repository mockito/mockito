/*
 * Copyright (c) 2012 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.internal.Incubating;

/**
 * The facility to create mocks. By default, a cglib/asm based implementation is used.
 * It is possible to configure your own mock maker so that dynamic proxies are created without cglib/asm.
 * For example, the android users can use a MockMaker that can work with Dalvik virtual machine
 * and hence bring Mockito to android apps developers.
 *
 * TODO: way more documentation and examples.
 */
@Incubating
public interface MockMaker {

    /**
     * Returns a new instance of {@code typeToMock} that implements the
     * interfaces of {@code extraInterfaces}. Invocations to the methods of the
     * returned instance will be delegated to {@code handler}.
     */
    <T> T createMock(Class<T> typeToMock, Class<?>[] extraInterfaces,
            MockitoInvocationHandler handler, MockSettingsInfo settings);

    /**
     * Returns the handler for the {@code mock}, or null if {@code mock} was not
     * a mock object created by {@link #createMock}.
     */
    MockitoInvocationHandler getHandler(Object mock);

    /**
     * Replaces the existing handler on {@code mock} with {@code newHandler}.
     */
    void resetMock(Object mock, MockitoInvocationHandler newHandler,
                          MockSettingsInfo settings);
}