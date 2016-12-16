/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.listeners.MockCreatedReport;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.Subscribe;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

public interface MockingProgress {
    
    void reportOngoingStubbing(OngoingStubbing<?> ongoingStubbing);

    OngoingStubbing<?> pullOngoingStubbing();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    void validateState();

    void reset();

    /**
     * Removes ongoing stubbing so that in case the framework is misused
     * state validation errors are more accurate
     */
    void resetOngoingStubbing();

    ArgumentMatcherStorage getArgumentMatcherStorage();
    
    void mockingStarted(Object mock, MockCreationSettings settings);

    /**
     * Registers the given Listener that will be notified when a mock object was created, see {@link #mockingStarted(Object, MockCreationSettings)}. 
     * The listener must provide a void method annotated with {@link Subscribe} and a sole 
     * {@link MockCreatedReport} parameter or implement the interface {@link MockCreationListener} . 
     * Here is an example:
     *  <pre>
     *  Object listener =  new Object(){
     *      public void logCreatedMocks(MockCreatedReport report){
     *         logger.log(report);
     *      }
     *  };
     *  </pre>
     *  <p>
     *  Note: Listeners implementing the interface {@link MockCreationListener} will be notified via {@link MockCreationListener#onMockCreated(Object, MockCreationSettings)}.  
     * 
     * @param listener must not be <code>null</code>
     */
    void addListener(Object listener);

    /**
     * Unregisters the given listener. It will not be notified about mock creations anymore.
     * @param listener
     */
    void removeListener(Object listener);

    void setVerificationStrategy(VerificationStrategy strategy);

    VerificationMode maybeVerifyLazily(VerificationMode mode);
}