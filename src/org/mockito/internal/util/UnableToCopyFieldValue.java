package org.mockito.internal.util;

class UnableToCopyFieldValue extends Exception {

    private static final long serialVersionUID = 1L;

    public UnableToCopyFieldValue(String message, Throwable t) {
        super(message, t);
    }
}