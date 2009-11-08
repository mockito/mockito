package org.mockito.internal.invocation;

import java.lang.reflect.Method;

public interface MockitoMethod {

    public String getName();

    public Class<?> getReturnType();

    public Class<?>[] getParameterTypes();

    public Class<?>[] getExceptionTypes();

    public boolean isVarArgs();

    public Method getJavaMethod();
}
