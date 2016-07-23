/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import static org.mockito.internal.exceptions.Reporter.unfinishedStubbing;
import static org.mockito.internal.exceptions.Reporter.unfinishedVerificationException;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.listeners.MockingProgressListener;
import org.mockito.internal.listeners.MockingStartedListener;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.listeners.StubbingListener;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();
    
    private OngoingStubbing<?> ongoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private Location stubbingInProgress = null;
    private MockingProgressListener listener;
    private VerificationStrategy verificationStrategy;

    public MockingProgressImpl() {
        this.verificationStrategy = getDefaultVerificationStrategy();
    }

    public static VerificationStrategy getDefaultVerificationStrategy() {
        return new VerificationStrategy() {
            public VerificationMode maybeVerifyLazily(VerificationMode mode) {
                return mode;
            }
        };
    }
    private StubbingListener stubbingListener;

    public void reportOngoingStubbing(OngoingStubbing iOngoingStubbing) {
        this.ongoingStubbing = iOngoingStubbing;
    }

    public OngoingStubbing<?> pullOngoingStubbing() {
        OngoingStubbing<?> temp = ongoingStubbing;
        ongoingStubbing = null;
        return temp;
    }
    
    public void verificationStarted(VerificationMode verify) {
        validateState();
        resetOngoingStubbing();
        verificationMode = new Localized(verify);
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.progress.MockingProgress#resetOngoingStubbing()
     */
    public void resetOngoingStubbing() {
        ongoingStubbing = null;
    }

    public VerificationMode pullVerificationMode() {
        if (verificationMode == null) {
            return null;
        }
        
        VerificationMode temp = verificationMode.getObject();
        verificationMode = null;
        return temp;
    }

    public void stubbingStarted() {
        validateState();
        stubbingInProgress = new LocationImpl();
    }

    public void validateState() {
        validateMostStuff();
        
        //validate stubbing:
        if (stubbingInProgress != null) {
            Location temp = stubbingInProgress;
            stubbingInProgress = null;
            throw unfinishedStubbing(temp);
        }
    }

    private void validateMostStuff() {
        //State is cool when GlobalConfiguration is already loaded
        //this cannot really be tested functionally because I cannot dynamically mess up org.mockito.configuration.MockitoConfiguration class
        GlobalConfiguration.validate();

        if (verificationMode != null) {
            Location location = verificationMode.getLocation();
            verificationMode = null;
            throw unfinishedVerificationException(location);
        }

        getArgumentMatcherStorage().validateState();
    }

    public void stubbingCompleted(Invocation invocation) {
        stubbingInProgress = null;
        getStubbingListener().newStubbing(invocation);
    }
    
    public String toString() {
        return  "iOngoingStubbing: " + ongoingStubbing + 
        ", verificationMode: " + verificationMode +
        ", stubbingInProgress: " + stubbingInProgress;
    }

    public void reset() {
        stubbingInProgress = null;
        verificationMode = null;
        getArgumentMatcherStorage().reset();
    }

    public ArgumentMatcherStorage getArgumentMatcherStorage() {
        return argumentMatcherStorage;
    }

    public void mockingStarted(Object mock, Class<?> classToMock) {
        if (listener instanceof MockingStartedListener) {
            ((MockingStartedListener) listener).mockingStarted(mock, classToMock);
        }
        validateMostStuff();
    }

    public void setListener(MockingProgressListener listener) {
        this.listener = listener;
    }

    public void setVerificationStrategy(VerificationStrategy strategy) {
        this.verificationStrategy = strategy;
    }

    public VerificationMode maybeVerifyLazily(VerificationMode mode) {
        return this.verificationStrategy.maybeVerifyLazily(mode);
    }

    public void setStubbingListener(StubbingListener stubbingListener) {
        this.stubbingListener = stubbingListener;
    }

    public StubbingListener getStubbingListener() {
        if (this.stubbingListener == null) {
            return NoOpStubbingListener.INSTANCE;
        }
        return this.stubbingListener;
    }
}