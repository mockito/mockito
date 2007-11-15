/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public class AssertionErrorWrapper extends RuntimeException {
    private final AssertionError error;

    public AssertionErrorWrapper(AssertionError error) {
        this.error = error;
    }

    public AssertionError getAssertionError() {
        return error;
    }
}
