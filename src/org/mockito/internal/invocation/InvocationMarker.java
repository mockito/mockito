/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

public class InvocationMarker {

    public void markVerified(List<Invocation> invocations, CapturesArgumensFromInvocation wanted) {
        for (Invocation invocation : invocations) {
            markVerified(invocation, wanted);
        }
    }

	public void markVerified(Invocation invocation, CapturesArgumensFromInvocation wanted) {
		invocation.markVerified();
		wanted.captureArgumentsFrom(invocation);
	}

    public void markVerifiedInOrder(List<Invocation> chunk, CapturesArgumensFromInvocation wanted, InOrderContext context) {
        markVerified(chunk, wanted);
        
        for (Invocation i : chunk) {
            context.markVerified(i);
        }
    }
}