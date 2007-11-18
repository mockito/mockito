package org.mockito.exceptions;

public class InvalidUseOfMatchersException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public InvalidUseOfMatchersException(String message) {
        super(  "\n" +
                message +
                "\n" +
                "Read about matchers: http://code.google.com/p/mockito/matchers");
    }
}
