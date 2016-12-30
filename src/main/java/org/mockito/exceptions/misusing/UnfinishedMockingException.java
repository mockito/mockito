package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class UnfinishedMockingException extends MockitoException {
    public UnfinishedMockingException(String message) {
        super(message);
    }
}
