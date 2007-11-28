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
                "Pass mocks that should be verified.");
    }

    public static void strictlyRequiresFamiliarMock() {
        throw new MockitoException(
                "\n" +
                "Strictly can only verify mocks that were passed in during creation of Strictly. E.g:" +
                "\n" +
                "strictly = createStrictOrderVerifier(mockOne)" +
                "\n" +
                "so strictly can only verify mockOne");
    }

    public static void mocksHaveToBePassedWhenCreatingStrictly() {
        throw new MockitoException(
                "\n" +
                "Method requires arguments." +
                "\n" +
                "Pass mocks that require strict order verification.");
    }
}
