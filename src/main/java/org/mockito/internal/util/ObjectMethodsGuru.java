/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.lang.reflect.Method;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;

public class ObjectMethodsGuru{

    private ObjectMethodsGuru() {
    }

    public static boolean isToStringMethod(Method method) {
        MockitoMethod m = new DelegatingMethod(method);
        return m.getReturnType() == String.class &&
               m.getParameterTypes().length == 0 &&
               "toString".equals(m.getName());
    }

    public static boolean isCompareToMethod(Method method) {
        return Comparable.class.isAssignableFrom(method.getDeclaringClass())
                && "compareTo".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == method.getDeclaringClass();
    }
}
