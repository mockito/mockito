/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.internal.verification.api.InOrderContext;

public class InvocationMarker {

    public void markVerified(List<InvocationImpl> invocations, CapturesArgumensFromInvocation wanted) {
        for (InvocationImpl invocation : invocations) {
            markVerified(invocation, wanted);
        }
    }

	public void markVerified(InvocationImpl invocation, CapturesArgumensFromInvocation wanted) {
		invocation.markVerified();
		wanted.captureArgumentsFrom(invocation);
	}

    public void markVerifiedInOrder(List<InvocationImpl> chunk, CapturesArgumensFromInvocation wanted, InOrderContext context) {
        markVerified(chunk, wanted);
        
        for (InvocationImpl i : chunk) {
            context.markVerified(i);
        }
    }
}