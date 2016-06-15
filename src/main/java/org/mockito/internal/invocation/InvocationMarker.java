/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

import java.util.List;

public class InvocationMarker {

    private InvocationMarker(){}
	
    public static void markVerified(List<Invocation> invocations, CapturesArgumentsFromInvocation wanted) {
        for (Invocation invocation : invocations) {
            markVerified(invocation, wanted);
        }
    }

    public static void markVerified(Invocation invocation, CapturesArgumentsFromInvocation wanted) {
        invocation.markVerified();
        wanted.captureArgumentsFrom(invocation);
    }

    public static void markVerifiedInOrder(List<Invocation> chunk, CapturesArgumentsFromInvocation wanted, InOrderContext context) {
        markVerified(chunk, wanted);

        for (Invocation i : chunk) {
            context.markVerified(i);
        }
    }
}