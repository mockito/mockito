/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.util.collections.ListUtil;

import java.util.List;

/**
 * Author: Szczepan Faber, created at: 4/3/11
 */
public class VerifiableInvocationsFinder {

    public List<InvocationImpl> find(List<?> mocks) {
        List<InvocationImpl> invocations = new AllInvocationsFinder().find(mocks);
        return ListUtil.filter(invocations, new RemoveIgnoredForVerification());
    }

    static class RemoveIgnoredForVerification implements ListUtil.Filter<InvocationImpl>{
        public boolean isOut(InvocationImpl i) {
            return i.isIgnoredForVerification();
        }
    }
}
