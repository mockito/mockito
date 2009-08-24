package org.mockito.internal.util;

import java.lang.reflect.Method;

public class ObjectMethodsGuru {

    public static boolean isToString(Method method) {
        return method.getReturnType() == String.class && method.getParameterTypes().length == 0
                && method.getName().equals("toString");
    }

    
}