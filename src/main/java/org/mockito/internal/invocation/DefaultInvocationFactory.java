/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DefaultInvocationFactory implements InvocationFactory {

    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Callable realMethod, Object... args) {
        RealMethod.FromCallable superMethod = new RealMethod.FromCallable(realMethod);
        return MockMethodInterceptor.createInvocation(target, method, args, superMethod, settings);
    }
}
