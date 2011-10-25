/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.io.Serializable;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.util.reflection.Whitebox;

public class SerializableMockitoMethodProxy extends AbstractMockitoMethodProxy implements Serializable {

    private static final long serialVersionUID = -5337859962876770632L;
    private final Class<?> c1;
    private final Class<?> c2;
    private final String desc;
    private final String name;
    private final String superName;
    private transient MethodProxy methodProxy;

    public SerializableMockitoMethodProxy(MethodProxy methodProxy) {
        Object info = Whitebox.getInternalState(methodProxy, "createInfo");
        c1 = (Class<?>) Whitebox.getInternalState(info, "c1");
        c2 = (Class<?>) Whitebox.getInternalState(info, "c2");
        desc = methodProxy.getSignature().getDescriptor();
        name = methodProxy.getSignature().getName();
        superName = methodProxy.getSuperName();
        this.methodProxy = methodProxy;
    }

    public MethodProxy getMethodProxy() {
        if (methodProxy == null)
            methodProxy = MethodProxy.create(c1, c2, desc, name, superName);
        return methodProxy;
    }
}