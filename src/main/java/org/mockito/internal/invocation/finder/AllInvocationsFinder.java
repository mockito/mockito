/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.finder;

import java.util.*;

import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.internal.stubbing.StubbingComparator;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

public class AllInvocationsFinder {

    private AllInvocationsFinder() {}

    /**
     * gets all invocations from mocks. Invocations are ordered earlier first.
     *
     * @param mocks mocks
     * @return invocations
     */
    public static List<Invocation> find(Iterable<?> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<Invocation>(new InvocationComparator());
        for (Object mock : mocks) {
            Collection<Invocation> fromSingleMock =
                    new DefaultMockingDetails(mock).getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }

        return new LinkedList<Invocation>(invocationsInOrder);
    }

    /**
     * Gets all stubbings from mocks. Invocations are ordered earlier first.
     *
     * @param mocks mocks
     * @return stubbings
     */
    public static Set<Stubbing> findStubbings(Iterable<?> mocks) {
        Set<Stubbing> stubbings = new TreeSet<Stubbing>(new StubbingComparator());
        for (Object mock : mocks) {
            // TODO due to the limited scope of static mocks they cannot be processed
            //  it would rather be required to trigger this stubbing control upon releasing
            //  the static mock.
            if (mock instanceof Class<?>) {
                continue;
            }
            Collection<? extends Stubbing> fromSingleMock =
                    new DefaultMockingDetails(mock).getStubbings();
            stubbings.addAll(fromSingleMock);
        }

        return stubbings;
    }
}
