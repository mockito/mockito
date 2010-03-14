package org.mockito.internal.verification.api;

import org.mockito.internal.invocation.Invocation;

public interface InOrderContext {

    boolean isVerified(Invocation invocation);

    void markVerified(Invocation i);

}
