/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.Constructors;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker.TypeMockability;

import java.io.Serializable;
import java.util.Collection;

@SuppressWarnings("unchecked")
public class MockCreationValidator {

    private final MockUtil mockUtil = new MockUtil();

    public void validateType(Class<?> classToMock) {
        TypeMockability typeMockability = mockUtil.typeMockabilityOf(classToMock);
        if (!typeMockability.mockable()) {
            new Reporter().cannotMockClass(classToMock, typeMockability.nonMockableReason());
        }
    }

    public void validateExtraInterfaces(Class<?> classToMock, Collection<Class<?>> extraInterfaces) {
        if (extraInterfaces == null) {
            return;
        }

        for (Class i : extraInterfaces) {
            if (classToMock == i) {
                new Reporter().extraInterfacesCannotContainMockedType(classToMock);
            }
        }
    }

    public void validateMockedType(Class<?> classToMock, Object spiedInstance) {
        if (classToMock == null || spiedInstance == null) {
            return;
        }
        if (!classToMock.equals(spiedInstance.getClass())) {
            new Reporter().mockedTypeIsInconsistentWithSpiedInstanceType(classToMock, spiedInstance);
        }
    }

    public void validateDelegatedInstance(Class<?> classToMock, Object delegatedInstance) {
        if (classToMock == null || delegatedInstance == null) {
            return;
        }
        if (delegatedInstance.getClass().isAssignableFrom(classToMock)) {
            new Reporter().mockedTypeIsInconsistentWithDelegatedInstanceType(classToMock, delegatedInstance);
        }
    }

    public void validateConstructorUse(boolean usingConstructor, SerializableMode mode) {
        if (usingConstructor && mode == SerializableMode.ACROSS_CLASSLOADERS) {
            new Reporter().usingConstructorWithFancySerializable(mode);
        }
    }
}
