package org.mockito.exceptions.verification;

import org.mockito.exceptions.base.MockitoAssertionError;

public class WantedButNotInvoked extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public WantedButNotInvoked(String message) {
        super(message);
    }
}