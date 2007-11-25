/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class InvalidUseOfMatchersException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public InvalidUseOfMatchersException(String message) {
        super(  "\n" +
                message +
                "\n" +
                "Read more: http://code.google.com/p/mockito/matchers");
    }
}
