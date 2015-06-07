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

    private static final long serialVersionUID = 1252072993982392338L;
    private Invocation invocation;

    public void add(final Invocation invocation) {
        this.invocation = invocation;
    }

    public void removeLast() {
        invocation = null;
    }

    public List<Invocation> getAll() {
        return Collections.emptyList();
    }

    public boolean isEmpty() {
        return invocation == null;
    }
}
