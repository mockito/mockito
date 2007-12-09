package org.mockito.exceptions.cause;

import org.mockito.exceptions.MockitoException;

public class UndesiredInvocation extends MockitoException {

    private static final long serialVersionUID = 1L;

    public UndesiredInvocation(String message) {
        super(message);
    }
}