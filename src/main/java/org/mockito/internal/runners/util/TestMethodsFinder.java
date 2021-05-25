/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import java.lang.reflect.Method;

import org.junit.Test;

public class TestMethodsFinder {

    private TestMethodsFinder() {}

    public static boolean hasTestMethods(Class<?> klass) {
        Method[] methods = klass.getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                return true;
            }
        }
        return false;
    }
}
