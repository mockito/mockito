package org.mockito.exceptions;

public class MissingMethodInvocationException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public MissingMethodInvocationException() {
        super("stub() requires an argument which has to be a proper method call on a mock object");
    }
}
