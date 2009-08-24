package org.mockito.internal.util;

import java.lang.reflect.Method;

public class ObjectMethodsGuru {

    public static boolean isToString(Method method) {
        return method.getReturnType() == String.class && method.getParameterTypes().length == 0
                && method.getName().equals("toString");
    }

    public boolean isEqualsMethod(Method method) {
        return method.getName().equals("equals") && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Object.class;
    }

    public boolean isHashCodeMethod(Method method) {
        return method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
    }
}