/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import static org.mockito.internal.util.collections.Sets.newSet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MockSettingsImpl<T> extends CreationSettings<T> implements MockSettings, MockCreationSettings<T> {

    private static final long serialVersionUID = 4475297236197939569L;
    private boolean useConstructor;
    private Object outerClassInstance;

    public MockSettings serializable() {
        return serializable(SerializableMode.BASIC);
    }

    public MockSettings serializable(final SerializableMode mode) {
        this.serializableMode = mode;
        return this;
    }

    public MockSettings extraInterfaces(final Class... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            new Reporter().extraInterfacesRequiresAtLeastOneInterface();
        }

        for (final Class i : extraInterfaces) {
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

    public MockSettings name(final String name) {
        this.name = name;
        return this;
    }

    public MockSettings spiedInstance(final Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    public MockSettings defaultAnswer(final Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        if (defaultAnswer == null) {
            new Reporter().defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public MockSettingsImpl stubOnly() {
        this.stubOnly = true;
        return this;
    }

    public MockSettings useConstructor() {
        this.useConstructor = true;
        return this;
    }

    public MockSettings outerInstance(final Object outerClassInstance) {
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

    public MockSettings invocationListeners(final InvocationListener... listeners) {
        if (listeners == null || listeners.length == 0) {
            new Reporter().invocationListenersRequiresAtLeastOneListener();
        }
        for (final InvocationListener listener : listeners) {
            if (listener == null) {
                new Reporter().invocationListenerDoesNotAcceptNullParameters();
            }
            this.invocationListeners.add(listener);
        }
        return this;
    }

    private boolean invocationListenersContainsType(final Class<?> clazz) {
        for (final InvocationListener listener : invocationListeners) {
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

    public MockCreationSettings<T> confirm(final Class<T> typeToMock) {
        return validatedSettings(typeToMock, this);
    }

    private static <T> CreationSettings<T> validatedSettings(final Class<T> typeToMock, final CreationSettings<T> source) {
        final MockCreationValidator validator = new MockCreationValidator();

        validator.validateType(typeToMock);
        validator.validateExtraInterfaces(typeToMock, source.getExtraInterfaces());
        validator.validateMockedType(typeToMock, source.getSpiedInstance());

        //TODO SF - add this validation and also add missing coverage
//        validator.validateDelegatedInstance(classToMock, settings.getDelegatedInstance());

        validator.validateSerializable(typeToMock, source.isSerializable());
        validator.validateConstructorUse(source.isUsingConstructor(), source.getSerializableMode());

        //TODO SF - I don't think we really need CreationSettings type
        final CreationSettings<T> settings = new CreationSettings<T>(source);
        settings.setMockName(new MockNameImpl(source.getName(), typeToMock));
        settings.setTypeToMock(typeToMock);
        settings.setExtraInterfaces(prepareExtraInterfaces(source));
        return settings;
    }

    private static Set<Class> prepareExtraInterfaces(final CreationSettings settings) {
        final Set<Class> interfaces = new HashSet<Class>(settings.getExtraInterfaces());
        if(settings.isSerializable()) {
            interfaces.add(Serializable.class);
        }
        return interfaces;
    }

}

