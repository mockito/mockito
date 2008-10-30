package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoException;

public class SmartNullPointerException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public SmartNullPointerException(String message, Throwable t) {
        super(message, t);
    }

    public SmartNullPointerException(String message) {
        super(message);
    }
}
