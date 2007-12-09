package org.mockito.exceptions;

public class UndesiredInvocation extends MockitoException {

    private static final long serialVersionUID = 1L;

    public UndesiredInvocation(String message) {
        super(message);
    }
}