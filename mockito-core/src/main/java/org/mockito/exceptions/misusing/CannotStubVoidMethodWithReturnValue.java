/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class CannotStubVoidMethodWithReturnValue extends MockitoException {
    public CannotStubVoidMethodWithReturnValue(String message) {
        super(message);
    }
}
