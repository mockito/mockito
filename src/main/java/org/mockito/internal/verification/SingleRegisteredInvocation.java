/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.mockito.invocation.Invocation;

public class SingleRegisteredInvocation implements RegisteredInvocations, Serializable {

    private Invocation invocation;

    @Override
    public void add(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override
    public void removeLast() {
        invocation = null;
    }

    @Override
    public List<Invocation> getAll() {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
        invocation = null;
    }

    @Override
    public boolean isEmpty() {
        return invocation == null;
    }
}
