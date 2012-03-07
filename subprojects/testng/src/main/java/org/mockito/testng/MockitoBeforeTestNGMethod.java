package org.mockito.testng;

import org.mockito.MockitoAnnotations;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import java.util.WeakHashMap;

public class MockitoBeforeTestNGMethod {

    private WeakHashMap<Object, Boolean> initializedInstances = new WeakHashMap<Object, Boolean>();

    /**
     * Initialize mocks.
     *
     * @param method Invoked method.
     * @param testResult TestNG Test Result
     */
    public void applyFor(IInvokedMethod method, ITestResult testResult) {
        initMocks(testResult);
        reinitCaptors(method, testResult);
    }

    private void reinitCaptors(IInvokedMethod method, ITestResult testResult) {
        if (method.isConfigurationMethod()) {
            return;
        }
        initializeCaptors(testResult.getInstance());
    }

    private void initMocks(ITestResult testResult) {
        if (alreadyInitialized(testResult.getInstance())) {
            return;
        }
        MockitoAnnotations.initMocks(testResult.getInstance());
        markAsInitialized(testResult.getInstance());
    }

    private void initializeCaptors(Object instance) {
        /*
         * TODO Refactor #processAnnotationOn methods out of DefaultAnnotationEngine
         */
    }

    private void markAsInitialized(Object instance) {
        initializedInstances.put(instance, true);
    }

    private boolean alreadyInitialized(Object instance) {
        return initializedInstances.containsKey(instance);
    }

}
