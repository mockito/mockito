/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

/**
 * Thrown when attempting to mock a class that is annotated with {@link org.mockito.DoNotMock}.
 */
public class DoNotMockException extends MockitoException {
    public DoNotMockException(String message) {
        super(message);
    }
}
