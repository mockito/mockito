/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.within;

import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;

class InternalInOrderContext implements InOrderContext {

    private final InOrderContext c;
    private boolean matched = false;

    public InternalInOrderContext(InOrderContext c) {
        this.c = c;
    }

    @Override
    public boolean isVerified(Invocation invocation) {
        return c.isVerified(invocation);
    }

    @Override
    public void markVerified(Invocation i) {
        c.markVerified(i);
    }

    public void markMatched() {
        matched = true;
    }

    public boolean matchedBefore() {
        return matched;
    }

    @Override
    public String toString() {
        return c.toString();
    }

}
