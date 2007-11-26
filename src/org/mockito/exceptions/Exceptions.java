/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class Exceptions {

    public static MockitoException mocksHaveToBePassedAsArguments() {
        throw new MockitoException(
                    "\n" +
                    "Method requires arguments." +
                    "\n" +
                    "Pass mocks that should be verified.");
    }
}
