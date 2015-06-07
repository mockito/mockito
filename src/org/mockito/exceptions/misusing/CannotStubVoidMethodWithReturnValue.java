package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class CannotStubVoidMethodWithReturnValue extends MockitoException {

    private static final long serialVersionUID = -2422014409790413960L;

    public CannotStubVoidMethodWithReturnValue(final String message) {
        super(message);
    }
}
