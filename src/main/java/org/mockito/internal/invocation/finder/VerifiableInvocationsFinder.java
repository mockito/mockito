/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.invocation.Invocation;

import java.util.List;

/**
 * Author: Szczepan Faber, created at: 4/3/11
 */
public class VerifiableInvocationsFinder {

    private VerifiableInvocationsFinder() {}

    public static List<Invocation> find(List<?> mocks) {
        List<Invocation> invocations = AllInvocationsFinder.find(mocks);
        return ListUtil.filter(invocations, new RemoveIgnoredForVerification());
    }

    private static class RemoveIgnoredForVerification implements Filter<Invocation>{
        public boolean isOut(Invocation invocation) {
            return invocation.isIgnoredForVerification();
        }
    }
}
