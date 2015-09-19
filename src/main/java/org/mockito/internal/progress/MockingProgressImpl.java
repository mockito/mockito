/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.listeners.MockingProgressListener;
import org.mockito.internal.listeners.MockingStartedListener;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private final Reporter reporter = new Reporter();
    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();
    
    IOngoingStubbing iOngoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private Location stubbingInProgress = null;
    private MockingProgressListener listener;

    public void reportOngoingStubbing(IOngoingStubbing iOngoingStubbing) {
        this.iOngoingStubbing = iOngoingStubbing;
    }

    public IOngoingStubbing pullOngoingStubbing() {
        IOngoingStubbing temp = iOngoingStubbing;
        iOngoingStubbing = null;
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
        iOngoingStubbing = null;
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
            reporter.unfinishedStubbing(temp);
        }
    }

    private void validateMostStuff() {
        //State is cool when GlobalConfiguration is already loaded
        //this cannot really be tested functionally because I cannot dynamically mess up org.mockito.configuration.MockitoConfiguration class
        GlobalConfiguration.validate();

        if (verificationMode != null) {
            Location location = verificationMode.getLocation();
            verificationMode = null;
            reporter.unfinishedVerificationException(location);
        }

        getArgumentMatcherStorage().validateState();
    }

    public void stubbingCompleted(Invocation invocation) {
        stubbingInProgress = null;
    }
    
    public String toString() {
        return  "iOngoingStubbing: " + iOngoingStubbing + 
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

    public void mockingStarted(Object mock, Class classToMock) {
        if (listener instanceof MockingStartedListener) {
            ((MockingStartedListener) listener).mockingStarted(mock, classToMock);
        }
        validateMostStuff();
    }

    public void setListener(MockingProgressListener listener) {
        this.listener = listener;
    }
}