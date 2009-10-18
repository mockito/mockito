package org.mockito.internal.runners.util;

import org.junit.Test;

import java.lang.reflect.Method;

public class TestMethodsFinder {
    public boolean hasTestMethods(Class<?> klass) {
        Method[] methods = klass.getMethods();
        for(Method m:methods) {
            if (m.isAnnotationPresent(Test.class)) {
                return true;
            }
        }
        return false;
    }
}
