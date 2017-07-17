/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.internal.creation.bytebuddy.InterceptedInvocation;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.util.Checks;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MockitoListener;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class DefaultMockitoFramework implements MockitoFramework {

    public MockitoFramework addListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().addListener(listener);
        return this;
    }

    public MockitoFramework removeListener(MockitoListener listener) {
        Checks.checkNotNull(listener, "listener");
        mockingProgress().removeListener(listener);
        return this;
    }

    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Callable realMethod, Object... args) {
        //TODO x we cannot use RealMethod here like that
        InterceptedInvocation.RealMethod.FromCallable superMethod = new InterceptedInvocation.RealMethod.FromCallable(realMethod);
        return MockMethodInterceptor.createInvocation(target, method, args, superMethod, settings);
    }
}
