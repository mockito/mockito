package org.mockito.internal.junit;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.listeners.StubbingLookupListener;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.listeners.MockCreationListener;
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
        //TODO #793 - when completed, we should be able to get rid of the CreationSettings casting below
        ((CreationSettings) settings).getStubbingLookupListeners().add(new StubbingLookupListener() {
            public void onStubbingLookup(Invocation invocation, MatchableInvocation stubbingFound) {
                if (stubbingFound == null) {
                    //If stubbing was not found for invocation it means that either the mock invocation was not stubbed or
                    //we have a stubbing arg mismatch.
                    List<Invocation> argMismatchStubbings = potentialArgMismatches(invocation);
                    if (!argMismatchStubbings.isEmpty()) {
                        Reporter.potentialStubbingProblemByJUnitRule(invocation, argMismatchStubbings);
                    }
                } else {
                    //when strict stubs are in use, every time a stub is realized in the code it is implicitly marked as verified
                    //this way, the users don't have to repeat themselves to verify stubbed invocations (DRY)
                    invocation.markVerified();
                }
            }
        });
    }

    private static List<Invocation> potentialArgMismatches(Invocation invocation) {
        List<Invocation> matchingStubbings = new LinkedList<Invocation>();
        Collection<Stubbing> stubbings = mockingDetails(invocation.getMock()).getStubbings();
        for (Stubbing s : stubbings) {
            if (!s.wasUsed() && s.getInvocation().getMethod().getName().equals(invocation.getMethod().getName())) {
                matchingStubbings.add(s.getInvocation());
            }
        }
        return matchingStubbings;
    }
}
