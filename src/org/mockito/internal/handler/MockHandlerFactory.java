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

    public InternalMockHandler create(MockCreationSettings settings) {
        InternalMockHandler handler = new MockHandlerImpl(settings);
        InternalMockHandler nullResultGuardian = new NullResultGuardian(handler);
        InternalMockHandler notifier = new InvocationNotifierHandler(nullResultGuardian, settings);

        return notifier;
    }
}
