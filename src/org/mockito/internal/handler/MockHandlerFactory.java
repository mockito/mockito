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
@SuppressWarnings({"rawtypes", "unchecked"})
public class MockHandlerFactory {

    public InternalMockHandler create(final MockCreationSettings settings) {
        final InternalMockHandler handler = new MockHandlerImpl(settings);
        final InternalMockHandler nullResultGuardian = new NullResultGuardian(handler);
        final InternalMockHandler notifier = new InvocationNotifierHandler(nullResultGuardian, settings);

        return notifier;
    }
}
