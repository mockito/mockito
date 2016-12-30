package org.mockito.exceptions.misusing;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.MockitoListener;

/**
 * Reported when instance of {@link org.mockito.listeners.MockitoListener}
 * is being added to Mockito (see {@link org.mockito.MockitoFramework})
 * and there is already a listener with this type registered.
 * <p>
 * Typically this indicates a user error - previous listener was not removed
 * according to the API specification - see {@link org.mockito.MockitoFramework#addListener(MockitoListener)}.
 */
public class RedundantListenerException extends MockitoException {
    public RedundantListenerException(String message) {
        super(message);
    }
}
