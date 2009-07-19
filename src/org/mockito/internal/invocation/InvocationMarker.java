package org.mockito.internal.invocation;

import java.util.List;

public class InvocationMarker {

    public void markVerified(List<Invocation> invocations, CapturesArgumensFromInvocation wanted) {
        for (Invocation invocation : invocations) {
            invocation.markVerified();
            wanted.captureArgumentsFrom(invocation);
        }
    }

    public void markVerifiedInOrder(List<Invocation> chunk, CapturesArgumensFromInvocation wanted) {
        markVerified(chunk, wanted);
        
        for (Invocation i : chunk) {
            i.markVerifiedInOrder();
        }
    }
}