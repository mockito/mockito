/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.LogInvocationsToStdOutListener;
import org.mockito.internal.util.MockName;
import org.mockito.invocation.InvocationListener;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class MockSettingsImpl implements MockSettings {

    private static final long serialVersionUID = 4475297236197939568L;
    private Class<?>[] extraInterfaces;
    private String name;
    private Object spiedInstance;
    private Answer<Object> defaultAnswer;
    private MockName mockName;
    private boolean serializable;
    private Set<InvocationListener> invocationListener = new HashSet<InvocationListener>();

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
		if (doesNotHaveListenerOfClass(LogInvocationsToStdOutListener.class)) {
			callback(new LogInvocationsToStdOutListener());
		}
		return this;
	}

	public MockSettings callback(InvocationListener listener) {
		validateListener(listener);
		this.invocationListener.add(listener);
		return this;
	}

	/** Returns the registered invocation listener, never {@code null} */
	public Collection<InvocationListener> getInvocationListener() {
		return this.invocationListener;
	}
	
	private boolean doesNotHaveListenerOfClass(Class<?> clazz) {
		for (InvocationListener listener : invocationListener) {
			if (listener.getClass().equals(clazz)) {
				return false;
			}
		}
		return true;
	}
	
	private void validateListener(InvocationListener listener) {
		if (listener == null) {
			new Reporter().nullInvocationListenerAdded();
		}
	}
}

