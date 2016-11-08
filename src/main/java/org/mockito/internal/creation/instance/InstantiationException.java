/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

public class InstantiationException extends MockitoException {

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
