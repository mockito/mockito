/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.jmock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.NoOp;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.cglib.MockitoNamingPolicy;
import org.objenesis.ObjenesisStd;

/**
 * Thanks to jMock guys for this handy class that wraps all the cglib magic. 
 */
public class ClassImposterizer  {

    public static final ClassImposterizer INSTANCE = new ClassImposterizer();
    
    private ClassImposterizer() {}
    
    private ObjenesisStd objenesis = new ObjenesisStd();
    
    private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES = new MockitoNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };
    
    private static final CallbackFilter IGNORE_BRIDGE_METHODS = new CallbackFilter() {
        public int accept(Method method) {
            return method.isBridge() ? 1 : 0;
        }
    };
    
    public boolean canImposterise(Class<?> type) {
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers()) && !type.isAnonymousClass();
    }
    
    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType, Class<?>... ancilliaryTypes) {
        try {
            setConstructorsAccessible(mockedType, true);
            Class<?> proxyClass = createProxyClass(mockedType);
            return mockedType.cast(createProxy(proxyClass, interceptor));
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }
    
    private void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }
    
    private <T> Class<?> createProxyClass(Class<?> mockedType) {
        if (mockedType == Object.class) {
            mockedType = ClassWithSuperclassToWorkAroundCglibBug.class;
        }
        
        Enhancer enhancer = new Enhancer() {
            @Override
            @SuppressWarnings("unchecked")
            protected void filterConstructors(Class sc, List constructors) {
                // Don't filter
            }
        };
        enhancer.setClassLoader(SearchingClassLoader.combineLoadersOf(mockedType));
        enhancer.setUseFactory(true);
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(prepend(mockedType));
        } else {
            enhancer.setSuperclass(mockedType);
        }
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class, NoOp.class});
        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        if (mockedType.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        } else {
            enhancer.setNamingPolicy(MockitoNamingPolicy.INSTANCE);
        }
        
        try {
            return enhancer.createClass(); 
        } catch (CodeGenerationException e) {
            if (Modifier.isPrivate(mockedType.getModifiers())) {
                throw new MockitoException("\n"
                        + "Mockito cannot mock this class: " + mockedType 
                        + ".\n"
                        + "Most likely it is a private class that is not visible by Mockito");
            }
            throw new MockitoException("\n"
                    + "Mockito cannot mock this class: " + mockedType 
                    + ".\n" 
                    + "Mockito can only mock visible & non-final classes");
        }
    }
    
    private Object createProxy(Class<?> proxyClass, final MethodInterceptor interceptor) {
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {interceptor, NoOp.INSTANCE});
        return proxy;
    }
    
    private Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length+1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }
    
    public static class ClassWithSuperclassToWorkAroundCglibBug {}
}