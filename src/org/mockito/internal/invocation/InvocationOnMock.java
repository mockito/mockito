package org.mockito.internal.invocation;

import java.lang.reflect.Method;

public interface InvocationOnMock {

    Object getMock();

    Method getMethod();

    Object[] getArguments();

}