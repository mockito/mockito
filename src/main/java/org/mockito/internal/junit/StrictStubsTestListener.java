package org.mockito.internal.junit;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.listeners.StubbingLookUpListener;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;

import java.util.*;

import static org.mockito.Mockito.mockingDetails;

/**
 * Test listener implementation that fails when there are unused stubbings
 */
class StrictStubsTestListener implements MockitoTestListener {

    private final Map<Object, MockCreationSettings> mocks = new HashMap<Object, MockCreationSettings>();

    public void testFinished(TestFinishedEvent event) {
        if (event.getFailure() == null) {
            //Detect unused stubbings:
            UnusedStubbings unused = new UnusedStubbingsFinder().getUnusedStubbings(mocks.keySet());
            unused.reportUnused();
        }
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.put(mock, settings);

        //It is not ideal that we modify the state of MockCreationSettings object
        //MockCreationSettings is intended to be an immutable view of the creation settings
        //In future, we should start passing MockSettings object to the creation listener
        settings.getStubbingLookUpListeners().add(new StubbingLookUpListener() {
            public void onStubbingLookUp(Invocation invocation, MatchableInvocation stubbingFound) {
                if (stubbingFound == null) {
                    List<Invocation> matchingStubbings = new LinkedList<Invocation>();
                    Collection<Stubbing> stubbings = mockingDetails(invocation.getMock()).getStubbings();
                    for (Stubbing s : stubbings) {
                        if (!s.wasUsed() && s.getInvocation().getMethod().getName().equals(invocation.getMethod().getName())) {
                            matchingStubbings.add(s.getInvocation());
                        }
                    }
                    if (!matchingStubbings.isEmpty()) {
                        Reporter.potentialStubbingProblemByJUnitRule(invocation, matchingStubbings);
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
