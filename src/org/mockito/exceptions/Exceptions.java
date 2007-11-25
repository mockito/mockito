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
