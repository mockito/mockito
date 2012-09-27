/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.internal.creation.MockitoMethodProxy;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;

import java.io.Serializable;

public class FilteredCGLIBProxyRealMethod implements RealMethod, HasCGLIBMethodProxy, Serializable {

    private static final long serialVersionUID = 3596550785818938496L;
    private final RealMethod realMethod;

    public FilteredCGLIBProxyRealMethod(MockitoMethodProxy methodProxy) {
        this(new CGLIBProxyRealMethod(methodProxy));
    }

    public FilteredCGLIBProxyRealMethod(RealMethod realMethod) {
        this.realMethod = realMethod;
    }

    public Object invoke(Object target, Object[] arguments) throws Throwable {
        try {
            return realMethod.invoke(target, arguments);
        } catch (Throwable t) {
            new ConditionalStackTraceFilter().filter(t);
            throw t;
        }
    }

    public MockitoMethodProxy getMethodProxy() {
        return ((HasCGLIBMethodProxy) realMethod).getMethodProxy();
    }
}