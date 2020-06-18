/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

/**
 * Reports unnecessary stubbings
 */
public class UnnecessaryStubbingsReporter implements MockCreationListener {

    private List<Object> mocks = new LinkedList<Object>();

    public void validateUnusedStubs(Class<?> testClass, RunNotifier notifier) {
        Collection<Invocation> unused =
                new UnusedStubbingsFinder().getUnusedStubbingsByLocation(mocks);
        if (unused.isEmpty()) {
            return; // whoa!!! All stubbings were used!
        }

        // Oups, there are unused stubbings
        Description unnecessaryStubbings =
                Description.createTestDescription(testClass, "unnecessary Mockito stubbings");
        notifier.fireTestFailure(
                new Failure(
                        unnecessaryStubbings,
                        Reporter.formatUnncessaryStubbingException(testClass, unused)));
    }

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        mocks.add(mock);
    }
}
