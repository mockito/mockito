/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.MockitoFramework;
import org.mockito.exceptions.base.MockitoException;

/**
 * Thrown when a mock is accessed after it has been disabled by
 * {@link MockitoFramework#clearInlineMocks()}.
 */
public class DisabledMockException extends MockitoException {
    public DisabledMockException() {
        this("Mock accessed after inline mocks were cleared");
    }

    /**
     * Creates the exception with diagnostic details about the disabled mock access.
     *
     * @param message the diagnostic message
     * @since 5.24.0
     */
    public DisabledMockException(String message) {
        super(message);
    }
}
