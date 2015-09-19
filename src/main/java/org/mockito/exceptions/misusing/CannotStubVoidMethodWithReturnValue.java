package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class CannotStubVoidMethodWithReturnValue extends MockitoException {
    public CannotStubVoidMethodWithReturnValue(String message) {
        super(message);
    }
}
