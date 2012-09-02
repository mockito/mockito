/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.collections.ListUtil.Filter;

//TODO this should be EmptyRegisteredInvocation, e.g. it should not store anything
public class RegisteredInvocationsStubOnly implements RegisteredInvocations, Serializable {

    private static final long serialVersionUID = -2674402327380736235L;

    private final ThreadLocal<Invocation> lastInvocation = new ThreadLocal<Invocation>();

    public void add(Invocation invocation) {
        this.lastInvocation.set(invocation);
    }

    public void removeLast() {
        this.lastInvocation.remove();
    }

    public List<Invocation> getAll() {
        if (this.lastInvocation.get() == null) {
            return Collections.emptyList();
        }

    	List<Invocation> copiedList = Collections.singletonList(this.lastInvocation.get());
        return ListUtil.filter(copiedList, new RemoveToString());
    }

    public boolean isEmpty() {
        return (this.lastInvocation.get() == null);
    }

    private static class RemoveToString implements Filter<Invocation> {
        public boolean isOut(Invocation invocation) {
            return new ObjectMethodsGuru().isToString(invocation.getMethod());
        }
    }

}
