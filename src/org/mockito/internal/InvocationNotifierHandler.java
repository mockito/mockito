/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.listeners.NotifiedMethodInvocationReport;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.listeners.InvocationListener;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.List;

/**
 * Handler, that call all listeners wanted for this mock, before delegating it
 * to the parameterized handler.
 *
 * Also imposterize MockHandler, delegate all call of MockHandlerInterface to the real mockHandler
 */
public class InvocationNotifierHandler<T> implements MockitoInvocationHandler, MockHandlerInterface<T>  {

    private List<InvocationListener> invocationListeners;
    private MockHandler<T> mockHandler;

    public InvocationNotifierHandler(MockHandler<T> mockHandler, MockSettingsImpl settings) {
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
                new Reporter().invocationListenerThrewException(listener, listenerThrowable);
            }
        }
	}

    private void notifyMethodCallException(Invocation invocation, Throwable exception) {
		for (InvocationListener listener : invocationListeners) {
            try {
                listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, exception));
            } catch(Throwable listenerThrowable) {
                new Reporter().invocationListenerThrewException(listener, listenerThrowable);
            }
        }
	}

    public MockSettingsImpl getMockSettings() {
        return mockHandler.getMockSettings();
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return mockHandler.voidMethodStubbable(mock);
    }

    public void setAnswersForStubbing(List<Answer> answers) {
        mockHandler.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return mockHandler.getInvocationContainer();
    }

}
