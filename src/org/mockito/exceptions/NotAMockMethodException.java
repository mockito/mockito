package org.mockito.exceptions;

public class NotAMockMethodException extends RuntimeException {

    public NotAMockMethodException() {
        super("stub() requires an argument which has to be a proper method call on a mock object");
    }
}
