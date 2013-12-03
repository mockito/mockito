/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.jmock;

import org.mockito.cglib.core.CodeGenerationException;
import org.mockito.cglib.core.NamingPolicy;
import org.mockito.cglib.core.Predicate;
import org.mockito.cglib.proxy.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.cglib.MockitoNamingPolicy;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import static org.mockito.internal.util.StringJoiner.join;

/**
 * Thanks to jMock guys for this handy class that wraps all the cglib magic. 
 */
public class ClassImposterizer  {

    public static final ClassImposterizer INSTANCE = new ClassImposterizer();
    
    private ClassImposterizer() {}
    
    //TODO: in order to provide decent exception message when objenesis is not found,
    //have a constructor in this class that tries to instantiate ObjenesisStd and if it fails then show decent exception that dependency is missing
    //TODO: for the same reason catch and give better feedback when hamcrest core is not found.
    private ObjenesisStd objenesis = new ObjenesisStd(new GlobalConfiguration().enableClassCache());
    
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
    
    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType, Collection<Class> ancillaryTypes) {
        return imposterise(interceptor, mockedType, ancillaryTypes.toArray(new Class[ancillaryTypes.size()]));
    }
    
    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType, Class<?>... ancillaryTypes) {
        Class<?> proxyClass = null;
        Object proxyInstance = null;
        try {
            setConstructorsAccessible(mockedType, true);
            proxyClass = createProxyClass(mockedType, ancillaryTypes);
            proxyInstance = createProxy(proxyClass, interceptor);
            return mockedType.cast(proxyInstance);
        } catch (ClassCastException cce) {
            // NPE unlikely to happen because CCE will only happen on the cast statement
            throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito proxy :",
                "  class to imposterize : '" + mockedType.getCanonicalName() + "', loaded by classloader : '" + mockedType.getClassLoader() + "'",
                "  imposterizing class : '" + proxyClass.getCanonicalName() + "', loaded by classloader : '" + proxyClass.getClassLoader() + "'",
                "  proxy instance class : '" + proxyInstance.getClass().getCanonicalName() + "', loaded by classloader : '" + proxyInstance.getClass().getClassLoader() + "'",
                "",
                "You might experience classloading issues, disabling the Objenesis cache *might* help (see MockitoConfiguration)"
            ), cce);
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }
    
    public void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }
    
    public Class<?> createProxyClass(Class<?> mockedType, Class<?>... interfaces) {
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
        Class<?>[] allMockedTypes = prepend(mockedType, interfaces);
		enhancer.setClassLoader(SearchingClassLoader.combineLoadersOf(allMockedTypes));
        enhancer.setUseFactory(true);
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(allMockedTypes);
        } else {
            enhancer.setSuperclass(mockedType);
            enhancer.setInterfaces(interfaces);
        }
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class, NoOp.class});
        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        if (mockedType.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        } else {
            enhancer.setNamingPolicy(MockitoNamingPolicy.INSTANCE);
        }

        enhancer.setSerialVersionUID(42L);
        
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
                    + "\n" 
                    + "Mockito can only mock visible & non-final classes."
                    + "\n" 
                    + "If you're not sure why you're getting this error, please report to the mailing list.", e);
        }
    }
    
    private Object createProxy(Class<?> proxyClass, final MethodInterceptor interceptor) {
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {interceptor, SerializableNoOp.SERIALIZABLE_INSTANCE });
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