package org.mockito.internal.invocation;

import org.mockito.invocation.Invocation;

import java.util.Comparator;

/**
 * Compares invocations based on the sequence number
 */
public class InvocationComparator implements Comparator<Invocation> {
    public int compare(Invocation o1, Invocation o2) {
        return Integer.valueOf(o1.getSequenceNumber()).compareTo(o2.getSequenceNumber());
    }
}