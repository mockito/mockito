package org.mockito.internal.creation;

public abstract class AbstractMockitoMethodProxy implements MockitoMethodProxy {

    public Object invokeSuper(Object target, Object[] arguments) throws Throwable {
        return getMethodProxy().invokeSuper(target, arguments);
    }
}