/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.internal.stubbing.BaseStubbing;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.invocation.MatchableInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KotlinInlineClassUtil {
    // When mocking function, returning inline class, its return type is
    // underlying type.
    // So, `thenReturn` calls fails, because of non-compatible types.
    public static boolean isInlineClassWithAssignableUnderlyingType(
            Class<?> inlineClass, Class<?> underlyingType) {
        try {
            // All inline classes have 'box-impl' method, which accepts
            // underlying type and returns inline class.
            inlineClass.getDeclaredMethod("box-impl", underlyingType);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static Object unboxInlineClass(Object boxedValue)
            throws InvocationTargetException, IllegalAccessException {
        Class<?> inlineClass = boxedValue.getClass();
        Method unboxImpl = null;
        for (Method declaredMethod : inlineClass.getDeclaredMethods()) {
            if (declaredMethod.getName().equals("unbox-impl")) {
                unboxImpl = declaredMethod;
                break;
            }
        }

        assert unboxImpl != null : inlineClass.getName() + " is not a Kotlin inline class";

        return unboxImpl.invoke(boxedValue);
    }

    private static Class<?> getReturnTypeOfInvocation(BaseStubbing<?> stubbing) {
        if (!(stubbing instanceof OngoingStubbingImpl<?>)) {
            return null;
        }
        // TODO: Hack to get return type of method
        OngoingStubbingImpl<?> ongoingStubbing = (OngoingStubbingImpl<?>) stubbing;
        if (ongoingStubbing.getRegisteredInvocations().size() == 0) return null;

        MatchableInvocation invocationForStubbing = ongoingStubbing.getInvocationForStubbing();
        return invocationForStubbing.getInvocation().getRawReturnType();
    }

    public static Object unboxUnderlyingValueIfNeeded(BaseStubbing<?> stubbing, Object value) {
        if (value != null) {
            Class<?> returnType = getReturnTypeOfInvocation(stubbing);
            if (returnType == null) return value;
            Class<?> valueType = value.getClass();
            if (valueType.isAssignableFrom(returnType)) return value;

            if (isInlineClassWithAssignableUnderlyingType(valueType, returnType)) {
                try {
                    return unboxInlineClass(value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return value;
                }
            } else {
                return value;
            }
        }
        return null;
    }
}
