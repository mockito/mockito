package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.MockitoMocking;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.quality.Strictness;

/**
 * This exception prevents the user from forgetting to use {@link MockitoMocking#finishMocking()}.
 * When {@link Mockito#startMocking(Object, Strictness)}
 * is used it needs to be concluded with {@link MockitoMocking#finishMocking()}.
 * <p>
 * For more information see Javadoc for {@link MockitoMocking}.
 *
 * @since 2.6.0
 */
public class UnfinishedMockingException extends MockitoException {
    public UnfinishedMockingException(String message) {
        super(message);
    }
}
