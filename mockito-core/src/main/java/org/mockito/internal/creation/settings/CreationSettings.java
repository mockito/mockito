/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.settings;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.MockType;
import org.mockito.mock.SerializableMode;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

public class CreationSettings<T> implements MockCreationSettings<T>, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;

    protected Class<T> typeToMock;
    protected transient Type genericTypeToMock;
    protected Set<Class<?>> extraInterfaces = new LinkedHashSet<>();
    protected String name;
    protected Object spiedInstance;
    protected Answer<Object> defaultAnswer;
    protected MockName mockName;
    protected SerializableMode serializableMode = SerializableMode.NONE;
    protected List<InvocationListener> invocationListeners = new ArrayList<>();

    // Other listeners in this class may also need concurrency-safe implementation. However, no
    // issue was reported about it.
    // If we do it, we need to understand usage patterns and choose the right concurrent
    // implementation.
    protected List<StubbingLookupListener> stubbingLookupListeners = new CopyOnWriteArrayList<>();

    protected List<VerificationStartedListener> verificationStartedListeners = new LinkedList<>();
    protected boolean stubOnly;
    protected boolean stripAnnotations;
    private boolean useConstructor;
    private Object outerClassInstance;
    private Object[] constructorArgs;
    protected Strictness strictness = null;
    protected String mockMaker;
    protected MockType mockType;

    public CreationSettings() {}

    @SuppressWarnings("unchecked")
    public CreationSettings(CreationSettings copy) {
        // TODO can we have a reflection test here? We had a couple of bugs here in the past.
        this.typeToMock = copy.typeToMock;
        this.genericTypeToMock = copy.genericTypeToMock;
        this.extraInterfaces = copy.extraInterfaces;
        this.name = copy.name;
        this.spiedInstance = copy.spiedInstance;
        this.defaultAnswer = copy.defaultAnswer;
        this.mockName = copy.mockName;
        this.serializableMode = copy.serializableMode;
        this.invocationListeners = copy.invocationListeners;
        this.stubbingLookupListeners = copy.stubbingLookupListeners;
        this.verificationStartedListeners = copy.verificationStartedListeners;
        this.stubOnly = copy.stubOnly;
        this.useConstructor = copy.isUsingConstructor();
        this.outerClassInstance = copy.getOuterClassInstance();
        this.constructorArgs = copy.getConstructorArgs();
        this.strictness = copy.strictness;
        this.stripAnnotations = copy.stripAnnotations;
        this.mockMaker = copy.mockMaker;
        this.mockType = copy.mockType;
    }

    @Override
    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    public CreationSettings<T> setTypeToMock(Class<T> typeToMock) {
        this.typeToMock = typeToMock;
        return this;
    }

    public CreationSettings<T> setGenericTypeToMock(Type genericTypeToMock) {
        this.genericTypeToMock = genericTypeToMock;
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

    @Override
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

    @Override
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
        return strictness == Strictness.LENIENT;
    }

    @Override
    public Strictness getStrictness() {
        return strictness;
    }

    @Override
    public String getMockMaker() {
        return mockMaker;
    }

    @Override
    public Type getGenericTypeToMock() {
        return genericTypeToMock;
    }

    @Override
    public MockType getMockType() {
        return mockType;
    }

    public void setMockType(MockType mockType) {
        this.mockType = mockType;
    }
}
