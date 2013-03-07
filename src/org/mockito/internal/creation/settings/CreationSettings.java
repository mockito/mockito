/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.settings;

import org.mockito.listeners.InvocationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ValidatorFactory;

/**
 * by Szczepan Faber, created at: 4/9/12
 */
public class CreationSettings<T> implements MockCreationSettings<T>, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;

    protected Class<T> typeToMock;
    protected Set<Class> extraInterfaces = new LinkedHashSet<Class>();
    protected String name;
    protected Object spiedInstance;
    protected Answer<Object> defaultAnswer;
    protected MockName mockName;
    protected boolean serializable;
    protected List<InvocationListener> invocationListeners = new ArrayList<InvocationListener>();
    protected boolean stubOnly;
    protected boolean validate;
    protected ValidatorFactory validatorFactory;

    public CreationSettings() {}

    @SuppressWarnings("unchecked")
    public CreationSettings(CreationSettings copy) {
        this.typeToMock = copy.typeToMock;
        this.extraInterfaces = copy.extraInterfaces;
        this.name = copy.name;
        this.spiedInstance = copy.spiedInstance;
        this.defaultAnswer = copy.defaultAnswer;
        this.mockName = copy.mockName;
        this.serializable = copy.serializable;
        this.invocationListeners = copy.invocationListeners;
        this.stubOnly = copy.stubOnly;
        this.validate = copy.validate;
        this.validatorFactory = copy.validatorFactory;
    }

    public Class<T> getTypeToMock() {
        return typeToMock;
    }

    public CreationSettings<T> setTypeToMock(Class<T> typeToMock) {
        this.typeToMock = typeToMock;
        return this;
    }

    public Set<Class> getExtraInterfaces() {
        return extraInterfaces;
    }

    public CreationSettings<T> setExtraInterfaces(Set<Class> extraInterfaces) {
        this.extraInterfaces = extraInterfaces;
        return this;
    }

    public String getName() {
        return name;
    }

    public Object getSpiedInstance() {
        return spiedInstance;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public MockName getMockName() {
        return mockName;
    }

    public CreationSettings<T> setMockName(MockName mockName) {
        this.mockName = mockName;
        return this;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public List<InvocationListener> getInvocationListeners() {
        return invocationListeners;
    }

    public boolean isStubOnly() {
        return stubOnly;
    }

    public boolean isValidate() {
        return validate;
    }

    public ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }

}
