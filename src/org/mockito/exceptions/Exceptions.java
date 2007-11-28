/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

public class Exceptions {

    public static void mocksHaveToBePassedAsArguments() {
        throw new MockitoException(
                "\n" +
                "Method requires arguments." +
                "\n" +
                "Pass mocks that should be verified, e.g:" +
                "\n" +
                "verifyNoMoreInteractions(mockOne, mockTwo)");
    }

    public static void strictlyRequiresFamiliarMock() {
        throw new MockitoException(
                "\n" +
                "Strictly can only verify mocks that were passed in during creation of Strictly. E.g:" +
                "\n" +
                "strictly = createStrictOrderVerifier(mockOne)" +
                "\n" +
                "strictly.verify(mockOne).doStuff()");
    }

    public static void mocksHaveToBePassedWhenCreatingStrictly() {
        throw new MockitoException(
                "\n" +
                "Method requires arguments." +
                "\n" +
                "Pass mocks that require strict order verification, e.g:" +
                "\n" +
                "createStrictOrderVerifier(mockOne, mockTwo)");
    }

    public static void checkedExceptionInvalid(Throwable t) {
        throw new MockitoException(
                "\n" +
        		"Checked exception is invalid for this method" +
        		"\n" +
        		"Invalid: " + t);
    }

    public static void cannotStubWithNullThrowable() {
        throw new MockitoException(
                "\n" +
                "Cannot stub with null throwable"                
                );
        
    }
}
