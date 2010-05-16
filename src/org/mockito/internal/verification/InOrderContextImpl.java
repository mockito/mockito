/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.util.IdentitySet;
import org.mockito.internal.verification.api.InOrderContext;

public class InOrderContextImpl implements InOrderContext {
    
    final IdentitySet verified = new IdentitySet();

    public boolean isVerified(Invocation invocation) {
        return verified.contains(invocation);
    }

    public void markVerified(Invocation i) {
        verified.add(i);
    }
}