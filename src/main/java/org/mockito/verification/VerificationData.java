/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

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
    InvocationMatcher getWanted();
}