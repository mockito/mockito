/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.debugging.Localized;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Location;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.VerificationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.internal.exceptions.Reporter.unfinishedStubbing;
import static org.mockito.internal.exceptions.Reporter.unfinishedVerificationException;

@SuppressWarnings("unchecked")
public class MockingProgressImpl implements MockingProgress {

    private final ArgumentMatcherStorage argumentMatcherStorage = new ArgumentMatcherStorageImpl();

    private OngoingStubbing<?> ongoingStubbing;
    private Localized<VerificationMode> verificationMode;
    private Location stubbingInProgress = null;
    private VerificationStrategy verificationStrategy;
    private final Set<MockitoListener> listeners = new LinkedHashSet<MockitoListener>();

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

    public void reportOngoingStubbing(OngoingStubbing iOngoingStubbing) {
        this.ongoingStubbing = iOngoingStubbing;
    }

    public OngoingStubbing<?> pullOngoingStubbing() {
        OngoingStubbing<?> temp = ongoingStubbing;
        ongoingStubbing = null;
        return temp;
    }

    @Override
    public Set<VerificationListener> verificationListeners() {
        final LinkedHashSet<VerificationListener> verificationListeners = new LinkedHashSet<VerificationListener>();

        for (MockitoListener listener : listeners) {
            if (listener instanceof VerificationListener) {
                verificationListeners.add((VerificationListener) listener);
            }
        }

        return verificationListeners;
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

    public void stubbingCompleted() {
        stubbingInProgress = null;
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

    public void mockingStarted(Object mock, MockCreationSettings settings) {
        for (MockitoListener listener : listeners) {
            if (listener instanceof MockCreationListener) {
                ((MockCreationListener) listener).onMockCreated(mock, settings);
            }
        }
        validateMostStuff();
    }

    public void addListener(MockitoListener listener) {
        for (MockitoListener existing : listeners) {
            if (existing.getClass().equals(listener.getClass())) {
                Reporter.redundantMockitoListener(listener.getClass().getSimpleName());
            }
        }
        this.listeners.add(listener);
    }

    public void removeListener(MockitoListener listener) {
        this.listeners.remove(listener);
    }

    public void setVerificationStrategy(VerificationStrategy strategy) {
        this.verificationStrategy = strategy;
    }

    public VerificationMode maybeVerifyLazily(VerificationMode mode) {
        return this.verificationStrategy.maybeVerifyLazily(mode);
    }

    public void clearListeners() {
        listeners.clear();
    }

     /*

     //TODO 545 thread safety of all mockito

     use cases:
        - single threaded execution throughout
        - single threaded mock creation, stubbing & verification, multi-threaded interaction with mock
        - thread per test case

     */
}
