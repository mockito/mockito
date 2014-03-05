/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;

public interface MockitoMethod extends AbstractAwareMethod {

    public String getName();

    public Class<?> getReturnType();

    public Class<?>[] getParameterTypes();

    public Class<?>[] getExceptionTypes();

    public boolean isVarArgs();

    public Method getJavaMethod();
}
