/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class InvocationsChunker {

    private final InvocationsFinder finder;

    public InvocationsChunker(InvocationsFinder invocationsFinder) {
        this.finder = invocationsFinder;
    }

    /**
     * chunk maybe empty, may have single invocation (usal case), or have
     * multiple the same invocations.
     * 
     * @param mocks
     * @return
     */
    public List<Invocation> getFirstUnverifiedInvocationChunk(List<Object> mocks) {
        Set<Invocation> allInvocationsInOrder = new TreeSet<Invocation>(new SequenceNumberComparator());
        
        List<Invocation> allInvocations = finder.allInvocationsInOrder(mocks);
        allInvocationsInOrder.addAll(allInvocations);
        
        return firstUnverifiedChunk(allInvocationsInOrder);
    }

    private List<Invocation> firstUnverifiedChunk(Set<Invocation> allInvocationsInOrder) {
        LinkedList<Invocation> chunk = new LinkedList<Invocation>();
        for (Invocation i : allInvocationsInOrder) {
            if (i.isVerifiedStrictly()) {
                continue;
            }
            
            if (chunk.isEmpty()) {
                chunk.add(i);
            } else if (chunk.getLast().equals(i)) {
                chunk.add(i);
            } else {
                break;
            }
        } 

        return chunk;
    }
    
    private final class SequenceNumberComparator implements Comparator<Invocation> {
        public int compare(Invocation o1, Invocation o2) {
            int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
            assert comparison != 0;
            return comparison;
        }
    }
}