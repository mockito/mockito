/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;

public interface MockitoMethod extends AbstractAwareMethod {

    String getName();

    Class<?> getReturnType();

    Class<?>[] getParameterTypes();

    Class<?>[] getExceptionTypes();

    boolean isVarArgs();

    Method getJavaMethod();
}
