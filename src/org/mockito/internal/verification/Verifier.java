/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public interface Verifier {
    void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode);

    boolean appliesTo(VerificationMode mode);
}
