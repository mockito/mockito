/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.jmock;

import static org.mockito.internal.util.StringJoiner.join;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import org.mockito.cglib.core.CodeGenerationException;
import org.mockito.cglib.core.NamingPolicy;
import org.mockito.cglib.core.Predicate;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.CallbackFilter;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.NoOp;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.cglib.MockitoNamingPolicy;
import org.objenesis.ObjenesisStd;

/**
 * Thanks to jMock guys for this handy class that wraps all the cglib magic. 
 */
public class ClassImposterizer  {

    public static final ClassImposterizer INSTANCE = new ClassImposterizer();
    
    private ClassImposterizer() {}
    
    //TODO: in order to provide decent exception message when objenesis is not found,
    //have a constructor in this class that tries to instantiate ObjenesisStd and if it fails then show decent exception that dependency is missing
    //TODO: for the same reason catch and give better feedback when hamcrest core is not found.
    private final ObjenesisStd objenesis = new ObjenesisStd(new GlobalConfiguration().enableClassCache());
    private final PartialMocker skipConstructor = new PartialMocker() {
	    @Override Object createProxy(Enhancer enhancer, Callback[] callbacks) {
	    	enhancer.setUseFactory(true);
	    	@SuppressWarnings("unchecked") // createClass() returns raw Class
			Factory factory = (Factory) objenesis.newInstance(enhancer.createClass());
	    	factory.setCallbacks(callbacks);
	    	return factory;
	    }
	    @Override boolean usesConstructor(Constructor<?> next) {
	    	return false;
	    }
	};
    
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
            throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito proxy :",
                "  class to imposterize : '" + describeClass(mockedType),
                "  imposterizing class : '" + describeClass(proxyClass),
                "  proxy instance class : '" + describeClass(proxyInstance),
                "",
                "You might experience classloading issues, disabling the Objenesis cache *might* help (see MockitoConfiguration)"
            ), cce);
        } finally {
            setConstructorsAccessible(mockedType, false);
        }
    }
    
    public <T> T instantiate(
    		MethodInterceptor interceptor,
    		Class<T> mockedType,
    		Class<?>[] interfaces,
    		Object enclosingInstance) {
    	if (mockedType.isInterface()) {
    		return imposterise(interceptor, mockedType);
    	}
        if(Modifier.isPrivate(mockedType.getModifiers())) {
            throw new IllegalArgumentException(
            		String.format("Cannot partial mock private %s %s. Please make the class non-private.", mockedType));
        }
        final UseConstructor useConstructor =
        		chooseMockConstructor(mockedType, enclosingInstance);
        final Constructor<?> superConstructor;
        try {
        	superConstructor = useConstructor.getConstructor(mockedType);
        } catch (NoSuchMethodException e) {
			return skipConstructor.partialMock(mockedType, interfaces, interceptor);
        }
        if (Modifier.isPrivate(superConstructor.getModifiers())) {
          // No good constructor, just skip
          return skipConstructor.partialMock(mockedType, interfaces, interceptor);
        }
        return new PartialMocker() {
			@Override Object createProxy(Enhancer enhancer, Callback[] callbacks) {
		    	  enhancer.setCallbacks(callbacks);
		    	  return useConstructor.construct(enhancer);
			}
			@Override boolean usesConstructor(Constructor<?> constructor) {
				return useConstructor.usesConstructor(constructor);
			}
        }.partialMock(mockedType, interfaces, interceptor);
    }

    private UseConstructor chooseMockConstructor(Class<?> toMock, Object enclosingInstance) {
      if (enclosingInstance != null) {
        return new UseInnerClassDefaultConstructor(toMock.getEnclosingClass(), enclosingInstance);
      } else {
        return new UseZeroArgConstructor();
      }
    }

    private static String describeClass(Class<?> type) {
        return type == null? "null" : type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null? "null" : describeClass(instance.getClass());
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
            @SuppressWarnings("rawtypes")
            protected void filterConstructors(Class sc, List constructors) {
                // Don't filter
            }
        };
		enhancer.setClassLoader(getCombinedClassLoader(mockedType, interfaces));
        enhancer.setUseFactory(true);
        if (mockedType.isInterface()) {
            enhancer.setSuperclass(Object.class);
            enhancer.setInterfaces(prepend(mockedType, interfaces));
        } else {
            enhancer.setSuperclass(mockedType);
            enhancer.setInterfaces(interfaces);
        }
        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class, NoOp.class});
        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        setNamingPolicy(enhancer, mockedType);

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

	static ClassLoader getCombinedClassLoader(Class<?> mockedType, Class<?>... interfaces) {
		return SearchingClassLoader.combineLoadersOf(prepend(mockedType, interfaces));
	}

	static void setNamingPolicy(Enhancer enhancer, Class<?> mockedType) {
		if (mockedType.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
        } else {
            enhancer.setNamingPolicy(MockitoNamingPolicy.INSTANCE);
        }
	}
    
    private Object createProxy(Class<?> proxyClass, final MethodInterceptor interceptor) {
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {interceptor, SerializableNoOp.SERIALIZABLE_INSTANCE });
        return proxy;
    }
    
    private static Class<?>[] prepend(Class<?> first, Class<?>... rest) {
        Class<?>[] all = new Class<?>[rest.length+1];
        all[0] = first;
        System.arraycopy(rest, 0, all, 1, rest.length);
        return all;
    }

    private interface UseConstructor {
    	boolean usesConstructor(Constructor<?> constructor);
    	Object construct(Enhancer enhancers);
    	Constructor<?> getConstructor(Class<?> type) throws NoSuchMethodException;
    }

    private static class UseZeroArgConstructor implements UseConstructor {

      @Override public boolean usesConstructor(Constructor<?> constructor) {
    	  return constructor.getParameterTypes().length == 0;
      }

      @Override public Object construct(Enhancer enhancer) {
    	  return enhancer.create();
      }

      @Override public Constructor<?> getConstructor(Class<?> type) throws NoSuchMethodException {
        return type.getDeclaredConstructor();
      }
    }

    private class UseInnerClassDefaultConstructor implements UseConstructor {

      private final Class<?> enclosingClass;
      private final Object enclosingInstance;

      UseInnerClassDefaultConstructor(Class<?> enclosingClass, Object enclosingInstance) {
        this.enclosingClass = enclosingClass;
        this.enclosingInstance = enclosingInstance;
      }

      @Override public boolean usesConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        return parameterTypes.length == 1 && parameterTypes[0] == enclosingClass;
      }

      @Override public Constructor<?> getConstructor(Class<?> type) throws NoSuchMethodException {
          return type.getDeclaredConstructor(enclosingClass);
      }

      @Override public Object construct(Enhancer enhancer) {
        return enhancer.create(new Class<?>[] {enclosingClass}, new Object[] {enclosingInstance});
      }
    }
    
    public static class ClassWithSuperclassToWorkAroundCglibBug {}
    
}