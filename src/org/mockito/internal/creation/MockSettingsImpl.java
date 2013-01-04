/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.internal.util.MockitoMock;
import org.mockito.internal.util.MockitoSpy;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newSet;

@SuppressWarnings("unchecked")
public class MockSettingsImpl<T> extends CreationSettings<T> implements MockSettings, MockCreationSettings<T> {

    private static final long serialVersionUID = 4475297236197939569L;

    public MockSettings serializable() {
        this.serializable = true;
        return this;
    }

    public MockSettings extraInterfaces(Class... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            new Reporter().extraInterfacesRequiresAtLeastOneInterface();
        }

        for (Class i : extraInterfaces) {
            if (i == null) {
                new Reporter().extraInterfacesDoesNotAcceptNullParameters();
            } else if (!i.isInterface()) {
                new Reporter().extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = newSet(extraInterfaces);
        return this;
    }

    public MockName getMockName() {
        return mockName;
    }

    public Set<Class> getExtraInterfaces() {
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
        if (defaultAnswer == null) {
            new Reporter().defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public MockSettingsImpl stubOnly() {
        this.stubOnly = true;
        return this;
    }

    public boolean isStubOnly() {
        return this.stubOnly;
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

    public boolean hasInvocationListeners() {
        return !invocationListeners.isEmpty();
    }

    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    public MockCreationSettings<T> confirm(Class<T> typeToMock) {
        return validatedSettings(typeToMock, this);
    }

    private static <T> CreationSettings<T> validatedSettings(Class<T> typeToMock, CreationSettings<T> source) {
        MockCreationValidator validator = new MockCreationValidator();

        validator.validateType(typeToMock);
        validator.validateExtraInterfaces(typeToMock, source.getExtraInterfaces());
        validator.validateMockedType(typeToMock, source.getSpiedInstance());

        //TODO SF - add this validation and also add missing coverage
//        validator.validateDelegatedInstance(classToMock, settings.getDelegatedInstance());

        validator.validateSerializable(typeToMock, source.isSerializable());

        CreationSettings<T> settings = new CreationSettings<T>(source);
        settings.setMockName(new MockNameImpl(source.getName(), typeToMock));
        settings.setTypeToMock(typeToMock);
        settings.setExtraInterfaces(prepareExtraInterfaces(source));
        return settings;
    }

    private static Set<Class> prepareExtraInterfaces(CreationSettings settings) {
        Set<Class> interfaces = new HashSet<Class>(settings.getExtraInterfaces());
        interfaces.add(MockitoMock.class);
        if(settings.isSerializable()) {
            interfaces.add(Serializable.class);
        }
        if (settings.getSpiedInstance() != null) {
            interfaces.add(MockitoSpy.class);
        }
        return interfaces;
    }

}

