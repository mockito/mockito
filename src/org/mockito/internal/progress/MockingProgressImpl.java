/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.DebuggingInfo;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.api.VerificationMode;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {
    
    private final Reporter reporter = new Reporter();
    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();
    
    private final DebuggingInfo debuggingInfo = new DebuggingInfo();

    IOngoingStubbing iOngoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private Location stubbingInProgress = null;

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
        stubbingInProgress = new Location();
    }

    public void validateState() {
        //State is cool when GlobalConfiguration is already loaded
        //this cannot really be tested functionally because I cannot dynamically mess up org.mockito.configuration.MockitoConfiguration class 
        GlobalConfiguration.validate();
        
        if (verificationMode != null) {
            Location location = verificationMode.getLocation();
            verificationMode = null;
            reporter.unfinishedVerificationException(location);
        }
        
        if (stubbingInProgress != null) {
            Location temp = stubbingInProgress;
            stubbingInProgress = null;
            reporter.unfinishedStubbing(temp);
        }
      
        getArgumentMatcherStorage().validateState();
    }

    public void stubbingCompleted(Invocation invocation) {
        debuggingInfo.addStubbedInvocation(invocation);        
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

    public DebuggingInfo getDebuggingInfo() {
        return debuggingInfo;
    }
}