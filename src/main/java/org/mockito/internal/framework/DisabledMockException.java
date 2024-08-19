package org.mockito.internal.framework;

/**
 * Thrown when a mock is accessed after it has been disabled by
 * {@link org.mockito.MockitoFramework#disableInlineMocks(String)}.
 */
public class DisabledMockException extends RuntimeException {
    public DisabledMockException(String message) {
        super(message);
    }
}
