package org.mockito.internal.junit;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.Stubbing;

/**
 * For given mocks, finds stubbing arg mismatches
 */
class ArgMismatchFinder {

    StubbingArgMismatches getStubbingArgMismatches(Iterable<Object> mocks) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : AllInvocationsFinder.find(mocks)) {
            if (i.stubInfo() != null) {
                continue;
            }
            for (Stubbing stubbing : AllInvocationsFinder.findStubbings(mocks)) {
                //method name & mock matches
                if (!stubbing.wasUsed() && stubbing.getInvocation().getMock() == i.getMock()
                        && stubbing.getInvocation().getMethod().getName().equals(i.getMethod().getName())) {
                    mismatches.add(i, stubbing.getInvocation());
                }
            }
        }
        return mismatches;
    }
}
