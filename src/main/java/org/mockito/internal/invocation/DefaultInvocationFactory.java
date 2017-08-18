package org.mockito.internal.invocation;

import org.mockito.internal.creation.bytebuddy.InterceptedInvocation;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DefaultInvocationFactory implements InvocationFactory {

    public Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Callable realMethod, Object... args) {
        //TODO x we cannot use RealMethod here like that
        InterceptedInvocation.RealMethod.FromCallable superMethod = new InterceptedInvocation.RealMethod.FromCallable(realMethod);
        return MockMethodInterceptor.createInvocation(target, method, args, superMethod, settings);
    }
}
