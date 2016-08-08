package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Stubbing;

/**
 * For given mocks, finds stubbing arg mismatches
 */
class ArgMismatchFinder {

    StubbingArgMismatches getStubbingArgMismatches(Iterable<Object> mocks) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : Mockito.debug().getInvocations(mocks)) {
            if (i.stubInfo() != null) {
                continue; //TODO 384 unit test
            }
            for (Stubbing stubbing : Mockito.debug().getStubbings(mocks)) {
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
