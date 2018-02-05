/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import org.mockito.exceptions.base.MockitoException;

/**
 * Exception generated when {@link Instantiator#newInstance(Class)} failed.
 *
 * <p>This exception is considered public even though it lives in private package.
 * In the next major version of Mockito, this class will be moved to public space.
 * </p>
 *
 * @since 2.14.0
 */
public class InstantiationException extends MockitoException {

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
