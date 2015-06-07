/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.io.Serializable;
import java.util.Collection;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.Constructors;
import org.mockito.mock.SerializableMode;

@SuppressWarnings("rawtypes")
public class MockCreationValidator {

    private final MockUtil mockUtil = new MockUtil();

    public void validateType(final Class classToMock) {
        if (!mockUtil.isTypeMockable(classToMock)) {
            new Reporter().cannotMockFinalClass(classToMock);
        }
    }

    public void validateExtraInterfaces(final Class classToMock, final Collection<Class> extraInterfaces) {
        if (extraInterfaces == null) {
            return;
        }

        for (final Class i : extraInterfaces) {
            if (classToMock == i) {
                new Reporter().extraInterfacesCannotContainMockedType(classToMock);
            }
        }
    }

    public void validateMockedType(final Class classToMock, final Object spiedInstance) {
        if (classToMock == null || spiedInstance == null) {
            return;
        }
        if (!classToMock.equals(spiedInstance.getClass())) {
            new Reporter().mockedTypeIsInconsistentWithSpiedInstanceType(classToMock, spiedInstance);
        }
    }

    public void validateDelegatedInstance(final Class classToMock, final Object delegatedInstance) {
        if (classToMock == null || delegatedInstance == null) {
            return;
        }
        if (delegatedInstance.getClass().isAssignableFrom(classToMock)) {
            new Reporter().mockedTypeIsInconsistentWithDelegatedInstanceType(classToMock, delegatedInstance);
        }
    }

    public void validateSerializable(final Class classToMock, final boolean serializable) {
        // We can't catch all the errors with this piece of code
        // Having a **superclass that do not implements Serializable** might fail as well when serialized
        // Though it might prevent issues when mockito is mocking a class without superclass.
        if(serializable
                && !classToMock.isInterface()
                && !(Serializable.class.isAssignableFrom(classToMock))
                && Constructors.noArgConstructorOf(classToMock) == null
                ) {
            new Reporter().serializableWontWorkForObjectsThatDontImplementSerializable(classToMock);
        }
    }

    public void validateConstructorUse(final boolean usingConstructor, final SerializableMode mode) {
        if (usingConstructor && mode == SerializableMode.ACROSS_CLASSLOADERS) {
            new Reporter().usingConstructorWithFancySerializable(mode);
        }
    }
}