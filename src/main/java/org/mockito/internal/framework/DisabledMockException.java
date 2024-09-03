package org.mockito.internal.framework;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * Thrown when a mock is accessed after it has been disabled by
 * {@link org.mockito.MockitoFramework#disableInlineMocks(String)}.
 */
public class DisabledMockException extends RuntimeException {
    public DisabledMockException() {
        super("Mock accessed after inline mocks were cleared");
    }

    public static MockHandler HANDLER = new MockHandler() {
        @Override
        public Object handle(Invocation invocation) {
            throw new DisabledMockException();
        }

        @Override
        public MockCreationSettings getMockSettings() {
            return null;
        }

        @Override
        public InvocationContainer getInvocationContainer() {
            return null;
        }
    };
}
