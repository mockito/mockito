/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.listeners.NotifiedMethodInvocationReport;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

/**
 * Handler, that call all listeners wanted for this mock, before delegating it
 * to the parameterized handler.
 *
 * Also imposterize MockHandlerImpl, delegate all call of InternalMockHandler to the real mockHandler
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class InvocationNotifierHandler<T> implements MockHandler, InternalMockHandler<T> {

    private static final long serialVersionUID = -4146835657113951837L;
    private final List<InvocationListener> invocationListeners;
    private final InternalMockHandler<T> mockHandler;

    public InvocationNotifierHandler(final InternalMockHandler<T> mockHandler, final MockCreationSettings settings) {
        this.mockHandler = mockHandler;
        this.invocationListeners = settings.getInvocationListeners();
    }

    public Object handle(final Invocation invocation) throws Throwable {
        try {
            final Object returnedValue = mockHandler.handle(invocation);
            notifyMethodCall(invocation, returnedValue);
            return returnedValue;
        } catch (final Throwable t){
            notifyMethodCallException(invocation, t);
            throw t;
        }
    }


    private void notifyMethodCall(final Invocation invocation, final Object returnValue) {
        for (final InvocationListener listener : invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, returnValue));
            } catch(final Throwable listenerThrowable) {
                new Reporter().invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    private void notifyMethodCallException(final Invocation invocation, final Throwable exception) {
        for (final InvocationListener listener : invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, exception));
            } catch(final Throwable listenerThrowable) {
                new Reporter().invocationListenerThrewException(listener, listenerThrowable);
            }
        }
    }

    public MockCreationSettings getMockSettings() {
        return mockHandler.getMockSettings();
    }

    public VoidMethodStubbable<T> voidMethodStubbable(final T mock) {
        return mockHandler.voidMethodStubbable(mock);
    }

    public void setAnswersForStubbing(final List<Answer> answers) {
        mockHandler.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return mockHandler.getInvocationContainer();
    }

}
