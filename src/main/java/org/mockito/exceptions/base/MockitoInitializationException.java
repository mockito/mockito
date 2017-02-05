/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

public class MockitoInitializationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MockitoInitializationException(String message) {
        super(message);
    }

    public MockitoInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
