package org.mockito.exceptions.cause;

import org.mockito.exceptions.base.MockitoException;

public class ShouldBeInvokedAfter extends MockitoException {

    private static final long serialVersionUID = 1L;
    
    public ShouldBeInvokedAfter(String message) {
        super(message);
    }
}
