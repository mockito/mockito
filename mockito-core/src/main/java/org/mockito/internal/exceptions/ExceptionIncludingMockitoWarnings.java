/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions;

public class ExceptionIncludingMockitoWarnings extends RuntimeException {
    private static final long serialVersionUID = -5925150219446765679L;

    public ExceptionIncludingMockitoWarnings(String message, Throwable throwable) {
        super(message, throwable);
    }
}
