/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.mockito.invocation.Invocation;

public class ZeroRegisteredInvocation implements RegisteredInvocations, Serializable {
    @Override
    public void add(Invocation invocation) {}

    @Override
    public void removeLast() {}

    @Override
    public List<Invocation> getAll() {
        return Collections.emptyList();
    }

    @Override
    public void clear() {}

    @Override
    public boolean isEmpty() {
        return true;
    }
}
