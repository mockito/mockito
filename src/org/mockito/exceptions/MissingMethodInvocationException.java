package org.mockito.exceptions;

public class MissingMethodInvocationException extends RuntimeException {

    public MissingMethodInvocationException() {
        super("stub() requires an argument which has to be a proper method call on a mock object");
    }
}
