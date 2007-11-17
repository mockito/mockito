/*
 * Copyright (c) 2003-2006 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.*;
import java.lang.reflect.InvocationHandler;
import java.util.*;

import net.sf.cglib.core.*;
import net.sf.cglib.proxy.*;

import org.mockito.*;
import org.mockito.internal.*;

/**
 * Factory generating a mock for a class.
 * <p>
 * Note that this class is stateful
 */
public class ClassProxyFactory<T> {

    public static interface MockMethodInterceptor extends MethodInterceptor {
        InvocationHandler getHandler();

        void setMockedMethods(Method[] mockedMethods);
    }

    @SuppressWarnings("unchecked")
    public T createProxy(Class<T> toMock, final MockAwareInvocationHandler handler) {

        // Dirty trick to fix MockitoObjectMethodsFilter
        // It will replace the equals, hashCode, toString methods it kept that
        // are the ones
        // from Object.class by the correct ones since they might have been
        // overloaded
        // in the mocked class.
    	if (!toMock.isInterface()) {
	        try {
	        	
	            updateMethod(handler, toMock.getMethod("equals",
	                    new Class[] { Object.class }));
	            updateMethod(handler, toMock.getMethod("hashCode", new Class[0]));
	            updateMethod(handler, toMock.getMethod("toString", new Class[0]));
	        } catch (NoSuchMethodException e) {
	            throw new RuntimeException(
	                    "We strangly failed to retrieve methods that always exist on an object...");
	        }
    	}

        MethodInterceptor interceptor = new MockMethodInterceptor() {

            private Set<Method> mockedMethods;

            public Object intercept(Object obj, Method method, Object[] args,
                    MethodProxy proxy) throws Throwable {
                if (method.isBridge()) {
                    return proxy.invokeSuper(obj, args);
                }
                if (mockedMethods != null && !mockedMethods.contains(method)) {
                    return proxy.invokeSuper(obj, args);
                }
                return handler.invoke(obj, method, args);
            }

            public InvocationHandler getHandler() {
                return handler;
            }

            public void setMockedMethods(Method[] mockedMethods) {
                this.mockedMethods = new HashSet<Method>(Arrays
                        .asList(mockedMethods));
            }
        };

        // Create the mock
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
        
        //TODO not tested
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

    private void updateMethod(InvocationHandler objectMethodsFilter,
            Method correctMethod) {
        Field methodField = retrieveField(MockitoObjectMethodsFilter.class,
                correctMethod.getName() + "Method");
        updateField(objectMethodsFilter, correctMethod, methodField);
    }

    private Field retrieveField(Class clazz, String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new InternalError(
                    "There must be some refactoring because the " + field
                            + " field was there...");
        }
    }

    private void updateField(Object instance, Object value, Field field) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new InternalError(
                    "Should be accessible since we set it ourselves");
        }
        field.setAccessible(accessible);
    }
}
