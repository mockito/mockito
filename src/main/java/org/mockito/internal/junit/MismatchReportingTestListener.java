/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.plugins.MockitoLogger;
import org.mockito.mock.MockCreationSettings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Reports stubbing argument mismatches to the supplied logger
 */
public class MismatchReportingTestListener implements MockitoTestListener {

    private final MockitoLogger logger;
    private List<Object> mocks = new LinkedList<Object>();

    public MismatchReportingTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    public void testFinished(TestFinishedEvent event) {
        Collection<Object> createdMocks = mocks;
        //At this point, we don't need the mocks any more and we can mark all collected mocks for gc
        //TODO make it better, it's easy to forget to clean up mocks and we still create new instance of list that nobody will read, it's also duplicated
        mocks = new LinkedList<Object>();

        if (event.getFailure() != null) {
            //print unused stubbings only when test succeeds to avoid reporting multiple problems and confusing users
            new ArgMismatchFinder().getStubbingArgMismatches(createdMocks).format(event.getTestName(), logger);
        }
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}
