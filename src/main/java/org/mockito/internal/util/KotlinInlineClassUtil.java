/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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
            @NotNull Class<?> inlineClass, @NotNull Class<?> underlyingType) {
        Method boxImpl = findBoxImplMethod(inlineClass);
        if (boxImpl == null) return false;

        Class<?> parameterType = boxImpl.getParameterTypes()[0];

        if (parameterType.isPrimitive() && underlyingType.isPrimitive()) {
            return Primitives.primitiveTypeOf(parameterType)
                    == Primitives.primitiveTypeOf(underlyingType);
        } else {
            return parameterType.isAssignableFrom(underlyingType);
        }
    }

    private static boolean isInlineClass(@NotNull Class<?> inlineClass) {
        return findBoxImplMethod(inlineClass) != null;
    }

    @Nullable
    private static Method findBoxImplMethod(@NotNull Class<?> inlineClass) {
        // All inline classes have 'box-impl' method, which accepts
        // underlying type and returns inline class.
        for (Method declaredMethod : inlineClass.getDeclaredMethods()) {
            if (declaredMethod.getName().equals("box-impl")) {
                return declaredMethod;
            }
        }
        return null;
    }

    @Nullable
    private static Object unboxInlineClass(@NotNull Object boxedValue)
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

    private static boolean returnsBoxedInlineClass(@NotNull BaseStubbing<?> stubbing) {
        Class<?> returnType = getReturnTypeOfInvocation(stubbing);
        if (returnType == null) return false;
        return isInlineClass(returnType);
    }

    @Nullable
    private static Class<?> getReturnTypeOfInvocation(@NotNull BaseStubbing<?> stubbing) {
        if (!(stubbing instanceof OngoingStubbingImpl<?>)) {
            return null;
        }
        // TODO: Hack to get return type of method
        OngoingStubbingImpl<?> ongoingStubbing = (OngoingStubbingImpl<?>) stubbing;
        if (ongoingStubbing.getRegisteredInvocations().size() == 0) return null;

        MatchableInvocation invocationForStubbing = ongoingStubbing.getInvocationForStubbing();
        return invocationForStubbing.getInvocation().getRawReturnType();
    }

    private static boolean returnsUnderlyingTypeOf(
            @NotNull BaseStubbing<?> stubbing, @NotNull Object value) {
        Class<?> returnType = getReturnTypeOfInvocation(stubbing);
        if (returnType == null) return false;
        return isInlineClassWithAssignableUnderlyingType(value.getClass(), returnType);
    }

    public static Object unboxUnderlyingValueIfNeeded(
            @NotNull BaseStubbing<?> stubbing, @Nullable Object value) {
        if (value != null
                && isInlineClass(value.getClass())
                && !returnsBoxedInlineClass(stubbing)
                && returnsUnderlyingTypeOf(stubbing, value)) {
            try {
                return KotlinInlineClassUtil.unboxInlineClass(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Do nothing. Fall through
            }
        }
        return value;
    }
}
