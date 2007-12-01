/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class MissingMethodInvocationException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public MissingMethodInvocationException(String message) {
        super(message);
    }
}
