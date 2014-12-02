package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

public class InstantationException extends MockitoException {

    public InstantationException(String message, Throwable cause) {
        super(message, cause);
    }
}
