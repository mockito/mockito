package org.mockito.internal.creation;

import org.mockito.cglib.proxy.MethodProxy;

public class DelegatingMockitoMethodProxy extends AbstractMockitoMethodProxy {

    private final MethodProxy methodProxy;

    public DelegatingMockitoMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    @Override
    protected MethodProxy getMethodProxy() {
        return methodProxy;
    }

}
