/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.invocation.Invocation;

import java.util.List;


public interface RegisteredInvocations {

    void add(Invocation invocation);

    void removeLast();

    List<Invocation> getAll();

    void clear();

    boolean isEmpty();

}
