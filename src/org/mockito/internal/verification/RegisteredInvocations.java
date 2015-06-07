/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import java.util.List;

import org.mockito.invocation.Invocation;


public interface RegisteredInvocations {

    void add(final Invocation invocation);

    void removeLast();

    List<Invocation> getAll();

    boolean isEmpty();

}
