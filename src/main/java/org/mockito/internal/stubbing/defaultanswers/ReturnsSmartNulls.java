/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.exceptions.Reporter.smartNullPointerException;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.mockito.Mockito;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

/**
 * Optional Answer that can be used with
 * {@link Mockito#mock(Class, Answer)}
 * <p>
 * This implementation can be helpful when working with legacy code. Unstubbed
 * methods often return null. If your code uses the object returned by an
 * unstubbed call you get a NullPointerException. This implementation of
 * Answer returns SmartNulls instead of nulls.
 * SmartNull gives nicer exception message than NPE because it points out the
 * line where unstubbed method was called. You just click on the stack trace.
 * <p>
 * ReturnsSmartNulls first tries to return ordinary return values (see
 * {@link ReturnsMoreEmptyValues}) then it tries to return SmartNull. If the
 * return type is not mockable (e.g. final) then ordinary null is returned.
 * <p>
 * ReturnsSmartNulls will be probably the default return values strategy in
 * Mockito 2.1.0
 */
public class ReturnsSmartNulls implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 7618312406617949441L;

    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = delegate.answer(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        Class<?> type = invocation.getMethod().getReturnType();

        final Type returnType = invocation.getMethod().getGenericReturnType();
        if (returnType instanceof TypeVariable) {
            type = findTypeFromGeneric(invocation, (TypeVariable) returnType);
            if (type != null) {
                defaultReturnValue = delegateChains(type);
            }
        }
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }

        if (type != null && !type.isPrimitive() && !Modifier.isFinal(type.getModifiers())) {
            final Location location = new LocationImpl();
            return Mockito.mock(type, new ThrowsSmartNullPointer(invocation, location));
        }
        return null;
    }

    /**
     * Try to resolve the result value using {@link ReturnsEmptyValues} and {@link ReturnsMoreEmptyValues}.
     *
     * This will try to use all parent class (superclass & interfaces) to retrieve the value..
     *
     * @param type the return type of the method
     * @return a non-null instance if the type has been resolve. Null otherwise.
     */
    private Object delegateChains(final Class<?> type) {
        final ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(type);

        if (result == null) {
            Class<?> emptyValueForClass = type;
            while (emptyValueForClass != null && result == null) {
                final Class<?>[] classes = emptyValueForClass.getInterfaces();
                for (Class<?> clazz : classes) {
                    result = returnsEmptyValues.returnValueFor(clazz);
                    if (result != null) {
                        break;
                    }
                }
                emptyValueForClass = emptyValueForClass.getSuperclass();
            }
        }

        if (result == null) {
            result = new ReturnsMoreEmptyValues().returnValueFor(type);
        }

        return result;
    }

    /**
     * Retrieve the expected type when it came from a primitive. If the type cannot be retrieve, return null.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the type or null if not found
     */
    private Class<?> findTypeFromGeneric(final InvocationOnMock invocation, final TypeVariable returnType) {
        // Class level
        final MockCreationSettings mockSettings = MockUtil.getMockHandler(invocation.getMock()).getMockSettings();
        final GenericMetadataSupport returnTypeSupport = GenericMetadataSupport
            .inferFrom(mockSettings.getTypeToMock())
            .resolveGenericReturnType(invocation.getMethod());
        final Class<?> rawType = returnTypeSupport.rawType();

        // Method level
        if (rawType == Object.class) {
            return findTypeFromGenericInArguments(invocation, returnType);
        }
        return rawType;
    }

    /**
     * Find a return type using generic arguments provided by the calling method.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the return type or null if the return type cannot be found
     */
    private Class<?> findTypeFromGenericInArguments(final InvocationOnMock invocation, final TypeVariable returnType) {
        final Type[] parameterTypes = invocation.getMethod().getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type argType = parameterTypes[i];
            if (returnType.equals(argType)) {
                return invocation.getArgument(i).getClass();
            }
            if (argType instanceof GenericArrayType) {
                argType = ((GenericArrayType) argType).getGenericComponentType();
                if (returnType.equals(argType)) {
                    return invocation.getArgument(i).getClass();
                }
            }
        }
        return null;
    }

    private static class ThrowsSmartNullPointer implements Answer {

        private final InvocationOnMock unstubbedInvocation;

        private final Location location;

        public ThrowsSmartNullPointer(InvocationOnMock unstubbedInvocation, Location location) {
            this.unstubbedInvocation = unstubbedInvocation;
            this.location = location;
        }

        public Object answer(InvocationOnMock currentInvocation) throws Throwable {
            if (isToStringMethod(currentInvocation.getMethod())) {
                return "SmartNull returned by this unstubbed method call on a mock:\n" +
                    unstubbedInvocation.toString();
            }

            throw smartNullPointerException(unstubbedInvocation.toString(), location);
        }
    }
}
