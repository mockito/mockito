/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.ListUtil.Filter;


public class RegisteredInvocations {

    private final LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    
    public void add(Invocation invocation) {
        invocations.add(invocation);
    }

    public void removeLast() {
        invocations.removeLast();
    }

    public List<Invocation> getVerifiableInvocations() {
        return ListUtil.filter(invocations, new RemoveToString());
    }
    
    private static class RemoveToString implements Filter<Invocation> {
        public boolean isOut(Invocation invocation) {
            return Invocation.isToString(invocation);
        }
    }
}