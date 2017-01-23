package org.mockito.internal.framework;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.junit.TestFinishedEvent;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.quality.Strictness;

public class DefaultMockitoSession implements MockitoSession {

    private final Object testClassInstance;
    private final UniversalTestListener listener;

    public DefaultMockitoSession(Object testClassInstance, Strictness strictness, MockitoLogger logger) {
        this.testClassInstance = testClassInstance;
        listener = new UniversalTestListener(strictness, logger);
        try {
            //So that the listener can capture mock creation events
            Mockito.framework().addListener(listener);
        } catch (RedundantListenerException e) {
            Reporter.unfinishedMockingSession();
        }
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void finishMocking() {
        //Cleaning up the state, we no longer need the listener hooked up
        //The listener implements MockCreationListener and at this point
        //we no longer need to listen on mock creation events. We are wrapping up the session
        Mockito.framework().removeListener(listener);

        //Emit test finished event so that validation such as strict stubbing can take place
        listener.testFinished(new TestFinishedEvent() {
            public Throwable getFailure() {
                return null;
            }
            public Object getTestClassInstance() {
                return testClassInstance;
            }
            public String getTestMethodName() {
                return null;
            }
        });

        //Finally, validate user's misuse of Mockito framework.
        Mockito.validateMockitoUsage();
    }
}
