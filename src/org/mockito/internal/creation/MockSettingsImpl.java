package org.mockito.internal.creation;

import org.mockito.MockSettings;
import org.mockito.ReturnValues;
import org.mockito.exceptions.Reporter;

public class MockSettingsImpl implements MockSettings {

    private ReturnValues returnValues;
    private Class<?>[] extraInterfaces;
    private String name;
    private Object spiedInstance;

    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
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

    public String getMockName() {
        return name;
    }

    public ReturnValues getReturnValues() {
        return returnValues;
    }

    public Class<?>[] getExtraInterfaces() {
        return extraInterfaces;
    }

    public Object getSpiedInstance() {
        return spiedInstance;
    }

    public MockSettings defaultBehavior(ReturnValues returnValues) {
        this.returnValues = returnValues;
        return this;
    }

    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }
}