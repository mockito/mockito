/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;


import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Factory generating a mock for a class.
 * <p>
 * Note that this class is stateful
 */
public class MockFactory<T> {

    public static interface MockMethodInterceptor extends MethodInterceptor {
        InvocationHandler getHandler();
    }

    @SuppressWarnings("unchecked")
    public T createMock(Class<T> toMock, final MockAwareInvocationHandler handler) {

        MethodInterceptor interceptor = new MockMethodInterceptor() {

            public Object intercept(Object obj, Method method, Object[] args,
                    MethodProxy proxy) throws Throwable {
                if (method.isBridge()) {
                    return proxy.invokeSuper(obj, args);
                }
                return handler.invoke(obj, method, args);
            }

            public InvocationHandler getHandler() {
                return handler;
            }
        };

        Enhancer enhancer = new Enhancer() {
            /**
             * Filter all private constructors but do not check that there are
             * some left
             */
            protected void filterConstructors(Class sc, List constructors) {
                CollectionUtils.filter(constructors, new VisibilityPredicate(
                        sc, true));
            }
        };
        
        if (toMock.isInterface()) {
			enhancer.setInterfaces(new Class[] { toMock });
		} else {
			enhancer.setSuperclass(toMock);
		}
        
        enhancer.setCallbackType(interceptor.getClass());

        Class mockClass = enhancer.createClass();
        Enhancer.registerCallbacks(mockClass, new Callback[] { interceptor });

        Factory mock;
        try {
            mock = (Factory) ObjenesisClassInstantiator.newInstance(mockClass);
        } catch (InstantiationException e) {
            throw new RuntimeException("Fail to instantiate mock for " + toMock
                    + " on " + System.getProperty("java.vm.vendor") + " JVM");
        }

        // This call is required. Cglib has some "magic code" making sure a
        // callback is used by only one instance of a given class. So only the
        // instance created right after registering the callback will get it.
        // However, this is done in the construtor which I'm bypassing to
        // allow class instantiation without calling a constructor.
        // Fortunatly, the "magic code" is also called in getCallback which is
        // why I'm calling it here mock.getCallback(0);
        
        mock.getCallback(0);

        handler.setMock(mock);
        return (T) mock;
    }
}