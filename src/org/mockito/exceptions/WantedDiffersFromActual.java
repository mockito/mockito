package org.mockito.exceptions;

public class WantedDiffersFromActual extends MockitoException {

    private static final long serialVersionUID = 1L;

    public WantedDiffersFromActual(String message) {
        super(message);
    }
}
