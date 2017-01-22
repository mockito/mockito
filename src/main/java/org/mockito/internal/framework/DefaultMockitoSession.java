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
            Mockito.framework().addListener(listener);
        } catch (RedundantListenerException e) {
            Reporter.unfinishedMockingSession();
        }
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void finishMocking() {
        Mockito.framework().removeListener(listener);
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
        Mockito.validateMockitoUsage();
    }
}
