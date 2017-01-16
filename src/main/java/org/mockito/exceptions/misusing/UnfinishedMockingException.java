package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.quality.Strictness;

/**
 * This exception prevents the user from forgetting to use {@link MockitoSession#finishMocking()}.
 * When {@link MockitoSession} is started is used
 * it needs to be concluded with {@link MockitoSession#finishMocking()}.
 * <p>
 * For more information see Javadoc for {@link MockitoSession}.
 *
 * @since 2.7.0
 */
public class UnfinishedMockingException extends MockitoException {
    public UnfinishedMockingException(String message) {
        super(message);
    }
}
