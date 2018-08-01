/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.settings;

import org.mockito.internal.listeners.StubbingLookupListener;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CreationSettings<T> implements MockCreationSettings<T>, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;

    protected Class<T> typeToMock;
    protected Set<Class<?>> extraInterfaces = new LinkedHashSet<Class<?>>();
    protected String name;
    protected Object spiedInstance;
    protected Answer<Object> defaultAnswer;
    protected MockName mockName;
    protected SerializableMode serializableMode = SerializableMode.NONE;
    protected List<InvocationListener> invocationListeners = new ArrayList<InvocationListener>();
    protected final List<StubbingLookupListener> stubbingLookupListeners = new ArrayList<StubbingLookupListener>();
    protected List<VerificationStartedListener> verificationStartedListeners = new LinkedList<VerificationStartedListener>();
    protected boolean stubOnly;
    protected boolean stripAnnotations;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;
    protected boolean lenient;

    public CreationSettings() {}

    @SuppressWarnings("unchecked")
    public CreationSettings(CreationSettings copy) {
        this.typeToMock = copy.typeToMock;
        this.extraInterfaces = copy.extraInterfaces;
        this.name = copy.name;
        this.spiedInstance = copy.spiedInstance;
        this.defaultAnswer = copy.defaultAnswer;
        this.mockName = copy.mockName;
        this.serializableMode = copy.serializableMode;
        this.invocationListeners = copy.invocationListeners;
        this.verificationStartedListeners = copy.verificationStartedListeners;
        this.stubOnly = copy.stubOnly;
        this.useConstructor = copy.isUsingConstructor();
        this.outerClassInstance = copy.getOuterClassInstance();
        this.constructorArgs = copy.getConstructorArgs();
        this.lenient = copy.lenient;
        this.stripAnnotations = copy.stripAnnotations;
    }

    @Override
    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    public CreationSettings<T> setTypeToMock(Class<T> typeToMock) {
        this.typeToMock = typeToMock;
        return this;
    }

    @Override
    public Set<Class<?>> getExtraInterfaces() {
        return extraInterfaces;
    }

    public CreationSettings<T> setExtraInterfaces(Set<Class<?>> extraInterfaces) {
        this.extraInterfaces = extraInterfaces;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getSpiedInstance() {
        return spiedInstance;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    @Override
    public MockName getMockName() {
        return mockName;
    }

    public CreationSettings<T> setMockName(MockName mockName) {
        this.mockName = mockName;
        return this;
    }

    public boolean isSerializable() {
        return serializableMode != SerializableMode.NONE;
    }

    public CreationSettings<T> setSerializableMode(SerializableMode serializableMode) {
        this.serializableMode = serializableMode;
        return this;
    }

    @Override
    public SerializableMode getSerializableMode() {
        return serializableMode;
    }

    @Override
    public List<InvocationListener> getInvocationListeners() {
        return invocationListeners;
    }

    @Override
    public List<VerificationStartedListener> getVerificationStartedListeners() {
        return verificationStartedListeners;
    }

    public List<StubbingLookupListener> getStubbingLookupListeners() {
        return stubbingLookupListeners;
    }

    @Override
    public boolean isUsingConstructor() {
        return useConstructor;
    }

    @Override
    public boolean isStripAnnotations() {
        return stripAnnotations;
    }

    @Override
    public Object[] getConstructorArgs() {
        return constructorArgs;
    }

    @Override
    public Object getOuterClassInstance() {
        return outerClassInstance;
    }

    @Override
    public boolean isStubOnly() {
        return stubOnly;
    }

    @Override
    public boolean isLenient() {
        return lenient;
    }
}
