/*
 * Copyright (c) 2003-2006 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.classextension;

import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.TestCase;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.easymock.ObjenesisClassInstantiator;
import org.junit.Test;

/**
 * This test case is used to make sure that the way cglib is used is providing the expected behavior
 */
public class CglibTest extends TestCase {

    /**
     * Check that an interceptor is used by only one instance of a class
     * @throws Exception
     */
    @Test public void test() throws Exception {

        Factory f1 = createMock();
        Factory f2 = createMock();
        
        assertNotSame(f1.getCallback(0), f2.getCallback(0));
    }

    @SuppressWarnings("unchecked")
    private Factory createMock() throws Exception {
        MethodInterceptor interceptor = new MethodInterceptor() {
            public Object intercept(Object obj, Method method, Object[] args,
                    MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, args);
            }
        };

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ArrayList.class);
        enhancer.setCallbackType(MethodInterceptor.class);
        
        Class mockClass = enhancer.createClass();
        
        Enhancer.registerCallbacks(mockClass, new Callback[] { interceptor });
                
        Factory f = (Factory) ObjenesisClassInstantiator.newInstance(mockClass);
        
        f.getCallback(0);
        
        return f;
    }
}
