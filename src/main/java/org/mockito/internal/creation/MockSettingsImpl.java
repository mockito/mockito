/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockNameImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.internal.exceptions.Reporter.defaultAnswerDoesNotAcceptNullParameter;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesAcceptsOnlyInterfaces;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesDoesNotAcceptNullParameters;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesRequiresAtLeastOneInterface;
import static org.mockito.internal.exceptions.Reporter.invocationListenersRequiresAtLeastOneListener;
import static org.mockito.internal.exceptions.Reporter.methodDoesNotAcceptParameter;
import static org.mockito.internal.util.collections.Sets.newSet;

@SuppressWarnings("unchecked")
public class MockSettingsImpl<T> extends CreationSettings<T> implements MockSettings, MockCreationSettings<T> {

    private static final long serialVersionUID = 4475297236197939569L;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;

    @Override
    public MockSettings serializable() {
        return serializable(SerializableMode.BASIC);
    }

    @Override
    public MockSettings serializable(SerializableMode mode) {
        this.serializableMode = mode;
        return this;
    }

    @Override
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

    @Override
    public MockName getMockName() {
        return mockName;
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        return extraInterfaces;
    }

    @Override
    public Object getSpiedInstance() {
        return spiedInstance;
    }

    @Override
    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    @Override
    public MockSettings defaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        if (defaultAnswer == null) {
            throw defaultAnswerDoesNotAcceptNullParameter();
        }
        return this;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public MockSettingsImpl<T> stubOnly() {
        this.stubOnly = true;
        return this;
    }

    @Override
    public MockSettings useConstructor(Object... constructorArgs) {
        Checks.checkNotNull(constructorArgs,
            "constructorArgs",
            "If you need to pass null, please cast it to the right type, e.g.: useConstructor((String) null)");
        this.useConstructor = true;
        this.constructorArgs = constructorArgs;
        return this;
    }

    @Override
    public MockSettings outerInstance(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
        return this;
    }

    @Override
    public MockSettings withoutAnnotations() {
        stripAnnotations = true;
        return this;
    }

    @Override
    public boolean isUsingConstructor() {
        return useConstructor;
    }

    @Override
    public Object getOuterClassInstance() {
        return outerClassInstance;
    }

    @Override
    public Object[] getConstructorArgs() {
        if (outerClassInstance == null) {
            return constructorArgs;
        }
        List<Object> resultArgs = new ArrayList<Object>(constructorArgs.length + 1);
        resultArgs.add(outerClassInstance);
        resultArgs.addAll(Arrays.asList(constructorArgs));
        return resultArgs.toArray(new Object[constructorArgs.length + 1]);
    }

    @Override
    public boolean isStubOnly() {
        return this.stubOnly;
    }

    @Override
    public MockSettings verboseLogging() {
        if (!invocationListenersContainsType(VerboseMockInvocationLogger.class)) {
            invocationListeners(new VerboseMockInvocationLogger());
        }
        return this;
    }

    @Override
    public MockSettings invocationListeners(InvocationListener... listeners) {
        if (listeners == null || listeners.length == 0) {
            throw invocationListenersRequiresAtLeastOneListener();
        }
        addListeners(listeners, invocationListeners, "invocationListeners");
        return this;
    }

    private static <T> void addListeners(T[] listeners, List<T> container, String method) {
        if (listeners == null) {
            throw methodDoesNotAcceptParameter(method, "null vararg array.");
        }
        for (T listener : listeners) {
            if (listener == null) {
                throw methodDoesNotAcceptParameter(method, "null listeners.");
            }
            container.add(listener);
        }
    }

    @Override
    public MockSettings verificationStartedListeners(VerificationStartedListener... listeners) {
        addListeners(listeners, this.verificationStartedListeners, "verificationStartedListeners");
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

    @Override
    public List<InvocationListener> getInvocationListeners() {
        return this.invocationListeners;
    }

    public boolean hasInvocationListeners() {
        return !invocationListeners.isEmpty();
    }

    @Override
    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    @Override
    public <T> MockCreationSettings<T> build(Class<T> typeToMock) {
        return validatedSettings(typeToMock, (CreationSettings<T>) this);
    }

    @Override
    public MockSettings lenient() {
        this.lenient = true;
        return this;
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
        //TODO do we really need to copy the entire settings every time we create mock object? it does not seem necessary.
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

