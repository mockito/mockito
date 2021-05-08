/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.answers.InvocationInfo;
import org.mockito.invocation.InvocationOnMock;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KotlinInlineClassUtil {
    private static Class<Annotation> jvmInlineAnnotation;

    static {
        try {
            jvmInlineAnnotation = (Class<Annotation>) Class.forName("kotlin.jvm.JvmInline");
        } catch (ClassNotFoundException e) {
            // Do nothing: kotlin is pre 1.5.0
        }
    }

    // When mocking function, returning inline class, its return type is
    // underlying type.
    // So, `thenReturn` calls fails, because of non-compatible types.
    public static boolean isInlineClassWithAssignableUnderlyingType(
            Class<?> inlineClass, Class<?> underlyingType) {
        try {
            // Since 1.5.0 inline classes have @JvmInline annotation
            if (jvmInlineAnnotation == null
                    || !inlineClass.isAnnotationPresent(jvmInlineAnnotation)) return false;

            // All inline classes have 'box-impl' method, which accepts
            // underlying type and returns inline class.
            // Make sure that the current inline class is also the class that is compatible with the
            // underlying type.
            // If we don't make this check, then we would potentially pass a mock of inline type A
            // into a method
            // that accepts inline type B.
            inlineClass.getDeclaredMethod("box-impl", underlyingType);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static Object unboxInlineClassIfPossible(Object boxedValue) {
        Class<?> inlineClass = boxedValue.getClass();
        try {
            Method unboxImpl = inlineClass.getDeclaredMethod("unbox-impl");
            return unboxImpl.invoke(boxedValue);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw Reporter.inlineClassWithoutUnboxImpl(inlineClass, e);
        }
    }

    public static Object unboxUnderlyingValueIfNeeded(InvocationOnMock invocation, Object value) {
        // Short path - Kotlin 1.5+ is not present.
        if (value == null || jvmInlineAnnotation == null) {
            return value;
        }

        Class<?> valueType = value.getClass();
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        Class<?> returnType = invocationInfo.getMethod().getReturnType();
        if (valueType.isAssignableFrom(returnType)) return value;

        if (isInlineClassWithAssignableUnderlyingType(valueType, returnType)) {
            return unboxInlineClassIfPossible(value);
        } else {
            return value;
        }
    }
}
