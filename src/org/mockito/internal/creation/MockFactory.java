/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Modifier;
import java.util.List;

import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import org.mockito.exceptions.Reporter;

/**
 * Factory generating a mock for a class.
 */
public class MockFactory<T> {

    @SuppressWarnings("unchecked")
    public T createMock(Class<T> toMock, final MethodInterceptorFilter filter, Object optionalInstance) {
        validateClass(toMock);
        Enhancer enhancer = createEnhancer(toMock);
        enhancer.setCallbackType(filter.getClass());

        Class mockClass = enhancer.createClass();
        
        Factory mock = createMock(mockClass);
        
        mock.setCallbacks(new Callback[] { filter });
        
        filter.setInstance(optionalInstance != null ? optionalInstance : mock);
        return (T) mock;
    }

    private void validateClass(Class<T> toMock) {
        if (Modifier.isFinal(toMock.getModifiers())) {
            new Reporter().cannotMockFinalClass(toMock);
        }
    }

    private Enhancer createEnhancer(Class<T> toMock) {
        Enhancer enhancer = new Enhancer() {
            @SuppressWarnings("unchecked")
            //Override default behavior which throws exception when no non-private constructors are left
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
        
        //This is required but I could not figure out the way to test it
        //See issue #11
        if (toMock.getSigners() != null) {
            enhancer.setNamingPolicy(ALLOWS_MOCKING_CLASSES_IN_SIGNED_PACKAGES);
        }

        //This is required to make (cglib + eclipse plugins testing) happy
        //See issue #11
        enhancer.setClassLoader(SearchingClassLoader.combineLoadersOf(toMock));
        
        return enhancer;
    }

    private Factory createMock(Class<?> mockClass) {
        Factory mock;
        try {
            mock = (Factory) ObjenesisClassInstantiator.newInstance(mockClass);
        } catch (InstantiationException e) {
            throw new RuntimeException("Fail to instantiate mock for " + mockClass
                    + " on " + System.getProperty("java.vm.vendor") + " JVM");
        }

        return mock;
    }
    
    private static final NamingPolicy ALLOWS_MOCKING_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };
}