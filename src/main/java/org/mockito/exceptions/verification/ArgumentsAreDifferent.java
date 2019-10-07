/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification;

import static org.mockito.internal.util.StringUtil.removeFirstLine;

import org.mockito.exceptions.base.MockitoAssertionError;

public class ArgumentsAreDifferent extends MockitoAssertionError {

    private static final long serialVersionUID = 1L;

    public ArgumentsAreDifferent(String message) {
        super(message);
    }

    /**
     * Three-arg constructor for compatibility with ExceptionFactory's three-arg
     * create method. This implementation simply ignores the second and third
     * arguments.
     *
     * @param message
     * @param wanted ignored
     * @param actual ignored
     */
    public ArgumentsAreDifferent(String message, String wanted, String actual) {
        this(message);
    }

    @Override
    public String toString() {
        return removeFirstLine(super.toString());
    }
}
