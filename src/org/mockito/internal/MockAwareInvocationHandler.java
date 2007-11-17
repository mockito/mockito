package org.mockito.internal;

import java.lang.reflect.InvocationHandler;

public interface MockAwareInvocationHandler extends InvocationHandler {
    void setMock(Object mock);
}
