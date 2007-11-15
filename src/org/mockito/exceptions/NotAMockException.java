package org.mockito.exceptions;

public class NotAMockException extends RuntimeException {

    public NotAMockException(Object object) {
        super("Not a mock: " + object.getClass().getName());
    }
}
