package org.mockito.internal;

import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * by Szczepan Faber, created at: 5/21/12
 */
public class MockHandlerFactory {

    public MockHandler create(MockCreationSettings settings) {
        return new InvocationNotifierHandler(
                new MockHandlerImpl(settings), settings);
    }
}
