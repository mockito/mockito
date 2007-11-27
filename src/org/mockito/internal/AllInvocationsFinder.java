package org.mockito.internal;

import java.util.*;

public class AllInvocationsFinder implements InvocationsFinder {

    public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
        List<Invocation> allInvocations = new LinkedList<Invocation>();
        for (Object mock : mocks) {
            List<Invocation> invocationsOfSingleMock = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocations.addAll(invocationsOfSingleMock);
        }
        return allInvocations;
    }
}
