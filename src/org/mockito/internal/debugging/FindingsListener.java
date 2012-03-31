/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;

public interface FindingsListener {
    void foundStubCalledWithDifferentArgs(InvocationImpl unused, InvocationMatcher unstubbed);

    void foundUnusedStub(InvocationImpl unused);

    void foundUnstubbed(InvocationMatcher unstubbed);
}
