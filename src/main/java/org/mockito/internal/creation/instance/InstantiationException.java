/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

/**
 * @deprecated since 2.15.4 because this internal class was leaking from the public API.
 * For information why deprecated, see {@link org.mockito.plugins.InstantiatorProvider2}.
 * Use {@link org.mockito.creation.instance.Instantiator} and {@link org.mockito.creation.instance.InstantiationException} types instead.
 * <p>
 * Exception generated when {@link Instantiator#newInstance(Class)} failed.
 */
@Deprecated
public class InstantiationException extends MockitoException {

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
