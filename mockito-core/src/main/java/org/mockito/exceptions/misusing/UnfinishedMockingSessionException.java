/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.session.MockitoSessionBuilder;

/**
 * This exception prevents the user from forgetting to use {@link MockitoSession#finishMocking()}.
 * When {@link MockitoSession} is started is used
 * it needs to be concluded with {@link MockitoSession#finishMocking()}.
 * <p>
 * For details on mocking session lifecycle see {@link MockitoSessionBuilder#startMocking()}
 * and {@link MockitoSession#finishMocking()}. For examples of use see {@link MockitoSession}.
 *
 * @since 2.7.0
 */
public class UnfinishedMockingSessionException extends MockitoException {
    public UnfinishedMockingSessionException(String message) {
        super(message);
    }
}
