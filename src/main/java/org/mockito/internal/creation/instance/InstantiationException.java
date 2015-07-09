package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

public class InstantiationException extends MockitoException {

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
