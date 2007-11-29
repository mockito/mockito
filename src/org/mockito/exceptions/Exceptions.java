/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;


/**
 * All messages in one place makes it easier to tune 
 * and amend the text.
 */
public class Exceptions {
    
    private static String join(String ... linesToBreak) {
        StringBuilder out = new StringBuilder("\n");
        for (String line : linesToBreak) {
            out.append(line).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }

    public static void mocksHaveToBePassedAsArguments() {
        throw new MockitoException(join(
                "Method requires arguments.",
                "Pass mocks that should be verified, e.g:",
                "verifyNoMoreInteractions(mockOne, mockTwo)"
                ));
    }

    public static void strictlyRequiresFamiliarMock() {
        throw new MockitoException(join(
                "Strictly can only verify mocks that were passed in during creation of Strictly. E.g:",
                "strictly = createStrictOrderVerifier(mockOne)",
                "strictly.verify(mockOne).doStuff()"
                ));
    }

    public static void mocksHaveToBePassedWhenCreatingStrictly() {
        throw new MockitoException(join(
                "Method requires arguments.",
                "Pass mocks that require strict order verification, e.g:",
                "createStrictOrderVerifier(mockOne, mockTwo)"
                ));
    }

    public static void checkedExceptionInvalid(Throwable t) {
        throw new MockitoException(join(
        		"Checked exception is invalid for this method",
        		"Invalid: " + t
        		));
    }

    public static void cannotStubWithNullThrowable() {
        throw new MockitoException(join(
                "Cannot stub with null throwable"
                ));
        
    }
    
    public static void wantedInvocationDiffersFromActual(String wanted, String actual, String message) {
        throw new VerificationError(join(
                    message,
                    "Wanted: " + wanted,
                    "Actual: " + actual
                ));
    }

    public static void wantedButNotInvoked(String wanted) {
        throw new VerificationError(join(
                    "Wanted but not invoked:",
                    wanted        
        ));
    }
}
