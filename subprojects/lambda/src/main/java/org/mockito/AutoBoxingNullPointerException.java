/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.base.MockitoException;

import static org.mockito.internal.util.StringUtil.join;

public class AutoBoxingNullPointerException extends MockitoException {
    AutoBoxingNullPointerException(Throwable t) {
        super(join("NullPointerException occured when trying to invoke referenced method.",
            "This is usually an artifact of trying to use `any()` or `invokedWithAnyArgs()` when primitive arguments are expected.",
            "For primitive arguments, use the corresponding `anyInt()` and alike to make sure auto-boxing does not throw NPE."), t);
    }
}
