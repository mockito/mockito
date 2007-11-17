package org.mockito.exceptions;

public class InvalidUseOfMatchersException extends IllegalStateException {

    public InvalidUseOfMatchersException(String message) {
        super(  "\n" +
                "Read about matchers: http://code.google.com/p/mockito/matchers" +
        		"\n" +
                message);
    }
}
