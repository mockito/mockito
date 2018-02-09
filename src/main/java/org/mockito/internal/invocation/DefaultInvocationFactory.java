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

    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, final Callable realMethod, Object... args) {
        RealMethod superMethod = new RealMethod.FromCallable(realMethod);
        return createInvocation(target, settings, method, superMethod, args);
    }

    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, RealMethodBehavior realMethod, Object... args) {
        RealMethod superMethod = new RealMethod.FromBehavior(realMethod);
        return createInvocation(target, settings, method, superMethod, args);
    }

    private Invocation createInvocation(Object target, MockCreationSettings settings, Method method, RealMethod superMethod, Object[] args) {
        return MockMethodInterceptor.createInvocation(target, method, args, superMethod, settings);
    }
}
