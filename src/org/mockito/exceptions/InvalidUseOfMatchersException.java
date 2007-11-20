package org.mockito.exceptions;

public class InvalidUseOfMatchersException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public InvalidUseOfMatchersException(String message) {
        super(  "\n" +
                message +
                "\n" +
                "Read more: http://code.google.com/p/mockito/matchers");
    }
}
