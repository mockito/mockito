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

    @Override
    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Object... args) {
        //TODO x we cannot use SuperMethod here like that
        InterceptedInvocation.SuperMethod.FromCallable superMethod = new InterceptedInvocation.SuperMethod.FromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new RuntimeException("not implemented yet");
            }
        });
        return MockMethodInterceptor.createInvocation(target, method, args, superMethod, settings);
    }
}
