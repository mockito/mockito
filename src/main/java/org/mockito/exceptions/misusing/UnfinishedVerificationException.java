/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class UnfinishedVerificationException extends MockitoException {
    
    private static final long serialVersionUID = 1L;

    public UnfinishedVerificationException(String message) {
        super(message);
    }
}
