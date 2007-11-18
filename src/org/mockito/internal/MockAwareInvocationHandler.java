package org.mockito.internal;

import java.lang.reflect.InvocationHandler;

public interface MockAwareInvocationHandler<T> extends InvocationHandler {
    void setMock(T mock);
}
