package org.mockito.internal.exceptions;

public class ExceptionIncludingMockitoWarnings extends RuntimeException {
    public ExceptionIncludingMockitoWarnings(String message, Throwable throwable) {
        super(message, throwable);
    }
}
