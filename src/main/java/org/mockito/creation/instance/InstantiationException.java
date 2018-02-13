/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.creation.instance;

import org.mockito.exceptions.base.MockitoException;

/**
 * Exception generated when {@link Instantiator#newInstance(Class)} failed.
 *
 * @since TODO
 */
public class InstantiationException extends MockitoException {

    /**
     * @since TODO
     */
    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
