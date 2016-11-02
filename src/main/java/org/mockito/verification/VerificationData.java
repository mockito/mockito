/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

import java.util.List;

/**
 * TODO Javadoc, include use case, @since tags
 */
public interface VerificationData {

    /**
     * TODO Javadoc, @since tags
     */
    List<Invocation> getAllInvocations();

    /**
     * TODO Javadoc, @since tags
     */
    MatchableInvocation getWanted();
}