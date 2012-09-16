/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

public class CannotVerifyStubOnlyMock extends MockitoException {

    public CannotVerifyStubOnlyMock(String message) {
        super(message);
    }
}
