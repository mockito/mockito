/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;

import java.util.List;

public class DefaultMockitoSession implements MockitoSession {

    private final String name;
    private final UniversalTestListener listener;

    public DefaultMockitoSession(List<Object> testClassInstances, String name, Strictness strictness, MockitoLogger logger) {
        this.name = name;
        listener = new UniversalTestListener(strictness, logger);
        try {
            //So that the listener can capture mock creation events
            Mockito.framework().addListener(listener);
        } catch (RedundantListenerException e) {
            Reporter.unfinishedMockingSession();
        }
        try {
            for (Object testClassInstance : testClassInstances) {
                MockitoAnnotations.initMocks(testClassInstance);
            }
        } catch (RuntimeException e) {
            //clean up in case 'initMocks' fails
            listener.setListenerDirty();
            throw e;
        }
    }

    @Override
    public void setStrictness(Strictness strictness) {
        listener.setStrictness(strictness);
    }

    @Override
    public void finishMocking() {
        finishMocking(null);
    }

    @Override
    public void finishMocking(final Throwable failure) {
        //Cleaning up the state, we no longer need the listener hooked up
        //The listener implements MockCreationListener and at this point
        //we no longer need to listen on mock creation events. We are wrapping up the session
        Mockito.framework().removeListener(listener);

        //Emit test finished event so that validation such as strict stubbing can take place
        listener.testFinished(new TestFinishedEvent() {
            @Override
            public Throwable getFailure() {
                return failure;
            }
            @Override
            public String getTestName() {
                return name;
            }
        });

        //Validate only when there is no test failure to avoid reporting multiple problems
        if (failure == null) {
            //Finally, validate user's misuse of Mockito framework.
            Mockito.validateMockitoUsage();
        }
    }
}
