/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

/**
 * Thrown when user supplied invalid method argument
 */
public class InvalidArgumentException extends MockitoException {

    public InvalidArgumentException(String message) {
        super(message);
    }
}
