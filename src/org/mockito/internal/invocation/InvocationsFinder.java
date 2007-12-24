/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

public interface InvocationsFinder {

    /**
     * gets all invocations from mocks. Invocations are ordered earlier first. 
     * 
     * @param mocks
     * @return
     */
    List<Invocation> getAllInvocations(List<? extends Object> mocks);
}