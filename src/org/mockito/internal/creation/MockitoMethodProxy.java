package org.mockito.internal.creation;

import org.mockito.cglib.proxy.MethodProxy;

public interface MockitoMethodProxy {

    Object invokeSuper(Object target, Object[] arguments) throws Throwable;

    MethodProxy getMethodProxy();

}