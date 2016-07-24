/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.exceptions.Reporter.invocationListenerThrewException;

import java.util.List;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.listeners.NotifiedMethodInvocationReport;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

/**
 * Handler, that call all listeners wanted for this mock, before delegating it
 * to the parameterized handler.
 *
 * Also imposterize MockHandlerImpl, delegate all call of InternalMockHandler to the real mockHandler
 */
class InvocationNotifierHandler<T> implements MockHandler, InternalMockHandler<T> {

    private final List<InvocationListener> invocationListeners;
    private final InternalMockHandler<T> mockHandler;

    public InvocationNotifierHandler(InternalMockHandler<T> mockHandler, MockCreationSettings<T> settings) {
        this.mockHandler = mockHandler;
        this.invocationListeners = settings.getInvocationListeners();
    }

    public Object handle(Invocation invocation) throws Throwable {
        try {
            Object returnedValue = mockHandler.handle(invocation);
            notifyMethodCall(invocation, returnedValue);
            return returnedValue;
        } catch (Throwable t){
            notifyMethodCallException(invocation, t);
            throw t;
        }
    }


    private void notifyMethodCall(Invocation invocation, Object returnValue) {
        for (InvocationListener listener : invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, returnValue));
            } catch(Throwable listenerThrowable) {
                throw invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    private void notifyMethodCallException(Invocation invocation, Throwable exception) {
        for (InvocationListener listener : invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, exception));
            } catch(Throwable listenerThrowable) {
                throw invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    public MockCreationSettings<T> getMockSettings() {
        return mockHandler.getMockSettings();
    }

    public void setAnswersForStubbing(List<Answer<?>> answers) {
        mockHandler.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return mockHandler.getInvocationContainer();
    }

}
