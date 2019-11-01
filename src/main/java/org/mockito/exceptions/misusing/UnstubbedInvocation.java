/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;

/**
 * Thrown when Mockito expects a given invocation to be stubbed.
 * See {@link org.mockito.quality.Strictness#STRICT_MOCKS}.
 *
 * @since 3.2.0
 */
public class UnstubbedInvocation extends MockitoException {
    public UnstubbedInvocation(String message) {
        super(message);
    }
}
