package org.mockito.internal.junit;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.invocation.Stubbing;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Finds unused stubbings
 */
class UnusedStubbingsFinder {
    UnusedStubbings getUnusedStubbings(Iterable<Object> mocks) {
        Collection<Stubbing> stubbings = (Collection) AllInvocationsFinder.findStubbings(mocks);

        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });

        return new UnusedStubbings(unused);
    }
}
