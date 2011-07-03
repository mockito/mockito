/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class NotAMockException extends MockitoException {

    private static final long serialVersionUID = 1L;

    public NotAMockException(String message) {
        super(message);
    }
}
