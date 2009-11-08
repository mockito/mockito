package org.mockito.internal.invocation;

public interface MockitoMethod {

    Class<?> getReturnType();

    Class<?>[] getParameterTypes();

    String getName();

    Class<?>[] getExceptionTypes();

    boolean isVarArgs();

    boolean isDeclaredOnInterface();

}
