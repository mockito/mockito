/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.MockSettings;

import static org.mockito.internal.exceptions.Reporter.*;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newSet;

@SuppressWarnings("unchecked")
public class MockSettingsImpl<T> extends CreationSettings<T> implements MockSettings, MockCreationSettings<T> {

    private static final long serialVersionUID = 4475297236197939569L;
    private boolean useConstructor;
    private Object outerClassInstance;

    public MockSettings serializable() {
        return serializable(SerializableMode.BASIC);
    }

    public MockSettings serializable(SerializableMode mode) {
        this.serializableMode = mode;
        return this;
    }

    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            throw extraInterfacesRequiresAtLeastOneInterface();
        }

        for (Class<?> i : extraInterfaces) {
            if (i == null) {
                throw extraInterfacesDoesNotAcceptNullParameters();
            } else if (!i.isInterface()) {
                throw extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = newSet(extraInterfaces);
        return this;
    }

    public MockName getMockName() {
        return mockName;
    }

    public Set<Class<?>> getExtraInterfaces() {
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
            throw defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public MockSettingsImpl<T> stubOnly() {
        this.stubOnly = true;
        return this;
    }

    public MockSettings useConstructor() {
        this.useConstructor = true;
        return this;
    }

    public MockSettings outerInstance(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
        return this;
    }

    public boolean isUsingConstructor() {
        return useConstructor;
    }

    public Object getOuterClassInstance() {
        return outerClassInstance;
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
            throw invocationListenersRequiresAtLeastOneListener();
        }
        for (InvocationListener listener : listeners) {
            if (listener == null) {
                throw invocationListenerDoesNotAcceptNullParameters();
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

        validator.validateConstructorUse(source.isUsingConstructor(), source.getSerializableMode());

        //TODO SF - I don't think we really need CreationSettings type
        CreationSettings<T> settings = new CreationSettings<T>(source);
        settings.setMockName(new MockNameImpl(source.getName(), typeToMock));
        settings.setTypeToMock(typeToMock);
        settings.setExtraInterfaces(prepareExtraInterfaces(source));
        return settings;
    }

    private static Set<Class<?>> prepareExtraInterfaces(CreationSettings settings) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>(settings.getExtraInterfaces());
        if(settings.isSerializable()) {
            interfaces.add(Serializable.class);
        }
        return interfaces;
    }

}

