package org.mockito.internal.stubbing;

import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.stubbing.Stubbing;

import java.util.Comparator;

/**
 * Compares stubbings based on sequence number
 */
public class StubbingComparator implements Comparator<Stubbing> {
    public int compare(Stubbing o1, Stubbing o2) {
        return new InvocationComparator().compare(o1.getInvocation(), o2.getInvocation());
    }
}
