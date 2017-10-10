/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.MockitoListener;

/**
 * Reported when instance of {@link org.mockito.listeners.MockitoListener}
 * is being added to Mockito (see {@link org.mockito.MockitoFramework})
 * and there is already a listener with this implementation type registered.
 * Note that it is ok to add multiple <strong>different</strong> implementations of the same listener interface type.
 * <p>
 * Indicates a user error - previous listener was not removed
 * according to the API specification - see {@link org.mockito.MockitoFramework#addListener(MockitoListener)}.
 *
 * @since 2.5.2
 */
public class RedundantListenerException extends MockitoException {
    public RedundantListenerException(String message) {
        super(message);
    }
}
