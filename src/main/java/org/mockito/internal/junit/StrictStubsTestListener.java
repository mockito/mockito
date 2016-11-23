package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.listeners.StubbingLookUpListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import java.util.*;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.internal.util.collections.ListUtil.filter;

/**
 * Test listener implementation that fails when there are unused stubbings
 */
class StrictStubsTestListener implements MockitoTestListener {

    private final Map<Object, MockCreationSettings> mocks = new HashMap<Object, MockCreationSettings>();

    public void beforeTest(Object testClassInstance, String testMethodName) {
        //TODO init mocks and validate mockito usage is duplicated in the listeners, refactor or make sure all is tested
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void afterTest(Throwable testFailure) {
        if (testFailure == null) {
            //Validate only when there is no test failure to avoid reporting multiple problems
            Mockito.validateMockitoUsage();

            //Detect unused stubbings:
            //TODO, extremely rudimentary, the exception message is awful
            Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks.keySet());

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

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.put(mock, settings);
        //TODO hack - modifying state of the listeners collection
        settings.getStubbingLookUpListeners().add(new StubbingLookUpListener() {
            public void onStubbingLookUp(Invocation invocation, MatchableInvocation stubbingFound) {
                if (stubbingFound == null) {
                    Collection<Stubbing> stubbings = mockingDetails(invocation.getMock()).getStubbings();
                    for (Stubbing s : stubbings) {
                        if (!s.wasUsed() && s.getInvocation().getMethod().getName().equals(invocation.getMethod().getName())) {
                            //TODO there are multiple stubbings
                            throw new MockitoAssertionError("Argument mismatch:\n - stubbing: " + s.getInvocation() +
                                    "\n - actual: " + invocation);
                        }
                    }
                } else {
                    //when strict stubs are in use, every time a stub is realized in the code it is implicitly marked as verified
                    //this way, the users don't have to repeat themselves to verify stubbed invocations (DRY)
                    invocation.markVerified();
                }
            }
        });
    }
}
