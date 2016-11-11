/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.LinkedList;
import java.util.List;

/**
 * Stubbing listener that is used in JUnit rules and detects stubbing problems.
 */
class RuleStubbingHintsReporter implements MockCreationListener {

    private final List<Object> mocks = new LinkedList<Object>();

    StubbingArgMismatches getStubbingArgMismatches() {
        return new ArgMismatchFinder().getStubbingArgMismatches(mocks);
    }

    UnusedStubbings getUnusedStubbings() {
        return new UnusedStubbingsFinder().getUnusedStubbings(mocks);
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }
}
