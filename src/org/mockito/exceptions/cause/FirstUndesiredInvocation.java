package org.mockito.exceptions.cause;

import org.mockito.exceptions.MockitoException;

public class FirstUndesiredInvocation extends MockitoException {

    private static final long serialVersionUID = 1L;

    public FirstUndesiredInvocation(String message) {
        super(message);
    }
}