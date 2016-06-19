/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import org.mockito.internal.InternalMockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * by Szczepan Faber, created at: 5/21/12
 */
public class MockHandlerFactory {

    public static <T> InternalMockHandler<T> createMockHandler(MockCreationSettings<T> settings) {
        InternalMockHandler<T> handler = new MockHandlerImpl<T>(settings);
        InternalMockHandler<T> nullResultGuardian = new NullResultGuardian<T>(handler);
        return new InvocationNotifierHandler<T>(nullResultGuardian, settings);
    }
}
