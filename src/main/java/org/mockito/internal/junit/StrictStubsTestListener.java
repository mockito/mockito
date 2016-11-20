package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.mockito.internal.util.collections.ListUtil.filter;

/**
 * Test listener implementation that fails when there are unused stubbings
 */
class StrictStubsTestListener implements MockitoTestListener {

    public void beforeTest(Object testClassInstance, String testMethodName) {
        //TODO init mocks and validate mockito usage is duplicated in the listeners, refactor or make sure all is tested
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void afterTest(Collection<Object> mocks, Throwable testFailure) {
        if (testFailure == null) {
            //Validate only when there is no test failure to avoid reporting multiple problems
            Mockito.validateMockitoUsage();

            //Detect unused stubbings:
            //TODO, extremely rudimentary, the exception message is awful
            Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks);

            List<Stubbing> unused = filter(stubbings, new ListUtil.Filter<Stubbing>() {
                public boolean isOut(Stubbing s) {
                    return s.wasUsed();
                }
            });

            if (!unused.isEmpty()) {
                throw new MockitoAssertionError("Unused stubbings detected: " + unused);
            }
        }
    }
}
