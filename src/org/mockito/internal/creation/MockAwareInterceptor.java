/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public interface MockAwareInterceptor<T> extends MethodInterceptor {

    Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable;
    
    void setInstance(T mock);
}