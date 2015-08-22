/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class DefaultRegisteredInvocations implements RegisteredInvocations, Serializable {

    private static final long serialVersionUID = -2674402327380736290L;
    private final LinkedList<Invocation> invocations = new LinkedList<Invocation>();

    public void add(Invocation invocation) {
        synchronized (invocations) {
            invocations.add(invocation);
        }
    }

    public void removeLast() {
        //TODO: add specific test for synchronization of this block (it is tested by InvocationContainerImplTest at the moment)
        synchronized (invocations) {
            if (! invocations.isEmpty()) {
                invocations.removeLast();
            }
        }
    }

    public List<Invocation> getAll() {
        List<Invocation> copiedList;
        synchronized (invocations) {
            copiedList = new LinkedList<Invocation>(invocations) ;
        }

        return ListUtil.filter(copiedList, new RemoveToString());
    }

    public void clear() {
        synchronized (invocations) {
            invocations.clear();
        }
    }

    public boolean isEmpty() {
        synchronized (invocations) {
            return invocations.isEmpty();
        }
    }

    private static class RemoveToString implements Filter<Invocation> {
        public boolean isOut(Invocation invocation) {
            return new ObjectMethodsGuru().isToString(invocation.getMethod());
        }
    }

}
