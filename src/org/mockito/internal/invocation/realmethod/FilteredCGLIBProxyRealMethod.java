package org.mockito.internal.invocation.realmethod;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.exceptions.base.ConditionalStackTraceFilter;

public class FilteredCGLIBProxyRealMethod implements RealMethod {

    private final RealMethod realMethod;

    public FilteredCGLIBProxyRealMethod(MethodProxy methodProxy) {
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
}