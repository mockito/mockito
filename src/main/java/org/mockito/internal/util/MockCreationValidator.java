/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotMockClass;
import static org.mockito.internal.exceptions.Reporter.extraInterfacesCannotContainMockedType;
import static org.mockito.internal.exceptions.Reporter.mockedTypeIsInconsistentWithDelegatedInstanceType;
import static org.mockito.internal.exceptions.Reporter.mockedTypeIsInconsistentWithSpiedInstanceType;
import static org.mockito.internal.exceptions.Reporter.usingConstructorWithFancySerializable;

import java.util.Collection;

import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker.TypeMockability;

@SuppressWarnings("unchecked")
public class MockCreationValidator {

    public void validateType(Class<?> classToMock) {
        TypeMockability typeMockability = MockUtil.typeMockabilityOf(classToMock);
        if (!typeMockability.mockable()) {
            throw cannotMockClass(classToMock, typeMockability.nonMockableReason());
        }
    }

    public void validateExtraInterfaces(Class<?> classToMock, Collection<Class<?>> extraInterfaces) {
        if (extraInterfaces == null) {
            return;
        }

        for (Class<?> i : extraInterfaces) {
            if (classToMock == i) {
                throw extraInterfacesCannotContainMockedType(classToMock);
            }
        }
    }

    public void validateMockedType(Class<?> classToMock, Object spiedInstance) {
        if (classToMock == null || spiedInstance == null) {
            return;
        }
        if (!classToMock.equals(spiedInstance.getClass())) {
            throw mockedTypeIsInconsistentWithSpiedInstanceType(classToMock, spiedInstance);
        }
    }

    public void validateDelegatedInstance(Class<?> classToMock, Object delegatedInstance) {
        if (classToMock == null || delegatedInstance == null) {
            return;
        }
        if (delegatedInstance.getClass().isAssignableFrom(classToMock)) {
            throw mockedTypeIsInconsistentWithDelegatedInstanceType(classToMock, delegatedInstance);
        }
    }

    public void validateConstructorUse(boolean usingConstructor, SerializableMode mode) {
        if (usingConstructor && mode == SerializableMode.ACROSS_CLASSLOADERS) {
            throw usingConstructorWithFancySerializable(mode);
        }
    }
}
