/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.mockito.exceptions.parents.MockitoException;

public class UnfinishedStubbingException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public UnfinishedStubbingException(String message) {
        super(message);
    }
}
