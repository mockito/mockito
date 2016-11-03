/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.util.List;

/**
 * @deprecated - This internal class leaks from the public API and therefore was deprecated.
 * Please use {@link org.mockito.verification.VerificationData} instead.
 *
 * Deprecated since @since@
 */
@Deprecated
public interface VerificationData {

    /**
     * Use {@link org.mockito.verification.VerificationData#getAllInvocations()} instead.
     */
    List<Invocation> getAllInvocations();

    /**
     * @deprecated - This internal method leaks internal class <code>InvocationMatcher</code>.
     * Please use {@link org.mockito.verification.VerificationData#getTarget()} instead.
     *
     * Deprecated since @since@
     */
    @Deprecated
    InvocationMatcher getWanted();
}