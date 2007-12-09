package org.mockito.exceptions.cause;

import org.mockito.exceptions.MockitoException;

public class WantedDiffersFromActual extends MockitoException {

    private static final long serialVersionUID = 1L;

    public WantedDiffersFromActual(String message) {
        super(message);
    }
}
