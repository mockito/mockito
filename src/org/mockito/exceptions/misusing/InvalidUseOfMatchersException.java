/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class InvalidUseOfMatchersException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public InvalidUseOfMatchersException(String message) {
        super(  "\n" +
                message +
                "\n" +
                "Invalid use of matchers - see javadoc for Matchers class." +
                "\n" +
                "Typically this exception occurs when matchers are combined with raw values:" +
                "\n" +
                "    verify(mock).someMethod(anyObject(), \"raw value\");" +
                "\n" +
                "When using matchers, all arguments have to be provided by matchers, eg:" +
                "\n" +
                "    verify(mock).someMethod(anyObject(), eq(\"raw value\"));");
    }
}
