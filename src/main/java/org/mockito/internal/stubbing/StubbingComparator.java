/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.Comparator;

import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.stubbing.Stubbing;

/**
 * Compares stubbings based on {@link InvocationComparator}
 */
public class StubbingComparator implements Comparator<Stubbing> {

    private final InvocationComparator invocationComparator = new InvocationComparator();

    public int compare(Stubbing o1, Stubbing o2) {
        return invocationComparator.compare(o1.getInvocation(), o2.getInvocation());
    }
}
