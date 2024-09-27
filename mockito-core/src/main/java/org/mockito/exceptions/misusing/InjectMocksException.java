/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

/**
 * Thrown when creation of test subject annotated with InjectMocks fails.
 */
public class InjectMocksException extends MockitoException {
    public InjectMocksException(String message, Throwable cause) {
        super(message, cause);
    }
}
