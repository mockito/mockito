package org.mockito.exceptions.cause;

import org.mockito.exceptions.parents.MockitoException;

public class TooLittleInvocations extends MockitoException {

    private static final long serialVersionUID = 1L;

    public TooLittleInvocations(String message) {
        super(message);
    }
}