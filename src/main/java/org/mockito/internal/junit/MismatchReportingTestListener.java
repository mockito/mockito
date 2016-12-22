package org.mockito.internal.junit;

import org.mockito.internal.junit.util.TestName;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.LinkedList;
import java.util.List;

/**
 * Reports stubbing argument mismatches to the supplied logger
 */
public class MismatchReportingTestListener implements MockitoTestListener {

    private final MockitoLogger logger;
    private final List<Object> mocks = new LinkedList<Object>();

    public MismatchReportingTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    public void testFinished(TestFinishedEvent event) {
        String testName = TestName.getTestName(event);
        if (event.getFailure() != null) {
            //print unused stubbings only when test succeeds to avoid reporting multiple problems and confusing users
            new ArgMismatchFinder().getStubbingArgMismatches(mocks).format(testName, logger);
        }
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}