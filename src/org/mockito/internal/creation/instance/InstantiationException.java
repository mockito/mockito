package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

public class InstantiationException extends MockitoException {

    private static final long serialVersionUID = 8161061096631959435L;

    public InstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
