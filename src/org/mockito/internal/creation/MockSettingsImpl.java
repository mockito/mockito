/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.MockName;
import org.mockito.listeners.InvocationListener;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockSettingsImpl implements MockSettings {

    private static final long serialVersionUID = 4475297236197939568L;
    private Class<?>[] extraInterfaces;
    private String name;
    private Object spiedInstance;
    private Answer<Object> defaultAnswer;
    private MockName mockName;
    private boolean serializable;
    private List<InvocationListener> invocationListeners = new ArrayList<InvocationListener>();

    public MockSettings serializable() {
        this.serializable = true;
        return this;
    }

    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            new Reporter().extraInterfacesRequiresAtLeastOneInterface();
        }
            
        for (Class<?> i : extraInterfaces) {
            if (i == null) {
                new Reporter().extraInterfacesDoesNotAcceptNullParameters();
            } else if (!i.isInterface()) {
                new Reporter().extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = extraInterfaces;
        return this;
    }

    public MockName getMockName() {
        return mockName;
    }

    public Class<?>[] getExtraInterfaces() {
        return extraInterfaces;
    }

    public Object getSpiedInstance() {
        return spiedInstance;
    }

    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    public MockSettings defaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        return this;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public boolean isSerializable() {
        return serializable;
    }
    
    public void initiateMockName(Class classToMock) {
        mockName = new MockName(name, classToMock);
    }

	public MockSettings verboseLogging() {
        if (!invocationListenersContainsType(VerboseMockInvocationLogger.class)) {
            invocationListeners(new VerboseMockInvocationLogger());
        }
        return this;
	}

    public MockSettings invocationListeners(InvocationListener... listeners) {
        if (listeners == null || listeners.length == 0) {
            new Reporter().invocationListenersRequiresAtLeastOneListener();
        }
        for (InvocationListener listener : listeners) {
            if (listener == null) {
                new Reporter().invocationListenerDoesNotAcceptNullParameters();
            }
            this.invocationListeners.add(listener);
        }
		return this;
	}

	private boolean invocationListenersContainsType(Class<?> clazz) {
		for (InvocationListener listener : invocationListeners) {
			if (listener.getClass().equals(clazz)) {
				return true;
			}
		}
		return false;
	}

    public List<InvocationListener> getInvocationListeners() {
        return this.invocationListeners;
    }

    public boolean containsInvocationListener(InvocationListener invocationListener) {
        return invocationListeners.contains(invocationListener);
    }

    public boolean hasInvocationListeners() {
        return !invocationListeners.isEmpty();
    }
}

