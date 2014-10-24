/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import static org.mockito.internal.util.StringJoiner.join;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collection;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.Instantiator;

/**
 * Inspired on jMock (thanks jMock guys!!!)
 */
class ClassImposterizer {
    private final Instantiator instantiator;

    public ClassImposterizer(Instantiator instantiator) {
        this.instantiator = instantiator;
    }
    
    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType, Collection<Class> ancillaryTypes) {
        return imposterise(interceptor, mockedType, ancillaryTypes.toArray(new Class[ancillaryTypes.size()]));
    }
    
    public <T> T imposterise(final MethodInterceptor interceptor, Class<T> mockedType, Class<?>... ancillaryTypes) {
        Class<? extends Factory> proxyClass = null;
        Object proxyInstance = null;
        try {
            setConstructorsAccessible(mockedType, true);
            proxyClass = ProxyMaker.makeProxyClass(mockedType, ancillaryTypes);
            proxyInstance = createProxy(proxyClass, interceptor);
            return mockedType.cast(proxyInstance);
        } catch (ClassCastException cce) {
            throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito proxy :",
                "  class to mock : " + describeClass(mockedType),
                "  created class : " + describeClass(proxyClass),
                "  proxy instance class : " + describeClass(proxyInstance),
                "  instance creation by : " + instantiator.getClass().getSimpleName(),
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
		if (Modifier.isPrivate(mockedType.getModifiers())) {
			throw new IllegalArgumentException(
					String.format(
							"Cannot partial mock private %s. Please make the class non-private.",
							mockedType));
		}
		final UseConstructor useConstructor = chooseMockConstructor(mockedType, enclosingInstance);
		final Constructor<?> superConstructor;
		try {
			superConstructor = useConstructor.getConstructor(mockedType);
		} catch (NoSuchMethodException e) {
			// No usable constructor. Just skip.
			return skipConstructor().make(mockedType, interfaces, interceptor);
		}
		if (Modifier.isPrivate(superConstructor.getModifiers())) {
			// Private constructor. Just skip
			return skipConstructor().make(mockedType, interfaces, interceptor);
		}
		return new ProxyMaker() {
			@Override Object createProxy(Enhancer enhancer, Callback... callbacks) {
				enhancer.setCallbacks(callbacks);
				return useConstructor.construct(enhancer);
			}
			@Override boolean usesConstructor(Constructor<?> constructor) {
				return useConstructor.usesConstructor(constructor);
			}
		}.make(mockedType, interfaces, interceptor);
	}

    private static String describeClass(Class<?> type) {
        return type == null? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(Object instance) {
        return instance == null? "null" : describeClass(instance.getClass());
    }

    //TODO this method does not belong here
    public static void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }
    
    private Object createProxy(Class<? extends Factory> proxyClass, MethodInterceptor interceptor) {
        Factory proxy = instantiator.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {interceptor, SerializableNoOp.SERIALIZABLE_INSTANCE });
        return proxy;
    }

	private ProxyMaker skipConstructor() {
	   return new ProxyMaker() {
		    @Override Object createProxy(Enhancer enhancer, Callback... callbacks) {
		    	enhancer.setUseFactory(true);
		    	@SuppressWarnings("unchecked") // createClass() returns raw Class
				Factory factory = (Factory) instantiator.newInstance(enhancer.createClass());
		    	factory.setCallbacks(callbacks);
		    	return factory;
		    }
		    @Override boolean usesConstructor(Constructor<?> next) {
		    	return false;
		    }
		};
	}

	private static UseConstructor chooseMockConstructor(Class<?> toMock, Object enclosingInstance) {
		if (enclosingInstance != null) {
	        if (!needsEnclosingInstance(toMock)) {
	    		throw new IllegalArgumentException(toMock + " is not non-static inner class.");
	    	}
	        if (!toMock.getEnclosingClass().isInstance(enclosingInstance)) {
	        	throw new IllegalArgumentException(
	        			toMock + " isn't inner class of " + enclosingInstance.getClass());
	        }
			return new UseInnerClassDefaultConstructor(
					toMock.getEnclosingClass(), enclosingInstance);
		} else {
	        if (needsEnclosingInstance(toMock)) {
	    		throw new IllegalArgumentException(toMock + " is non-static inner class.");
	    	}
			return new UseZeroArgConstructor();
		}
	}

	private static <T> boolean needsEnclosingInstance(Class<T> type) {
		return type.getEnclosingClass() != null && !Modifier.isStatic(type.getModifiers());
	}

	private interface UseConstructor {
		/** Returns true if {@code constructor} will be invoked to construct the mock. */
		boolean usesConstructor(Constructor<?> constructor);

		/** Constructs the mock instance using the class enhanced by {@code enhancer}. */
		Object construct(Enhancer enhancers);

		/** Returns the constructor to be invoked. */
		Constructor<?> getConstructor(Class<?> type) throws NoSuchMethodException;
	}

	private static class UseZeroArgConstructor implements UseConstructor {

		@Override public boolean usesConstructor(Constructor<?> constructor) {
			return constructor.getParameterTypes().length == 0;
		}

		@Override public Object construct(Enhancer enhancer) {
			return enhancer.create();
		}

		@Override public Constructor<?> getConstructor(Class<?> type)
				throws NoSuchMethodException {
			return type.getDeclaredConstructor();
		}
	}

	private static class UseInnerClassDefaultConstructor implements UseConstructor {
		private final Class<?> enclosingClass;
		private final Object enclosingInstance;

		UseInnerClassDefaultConstructor(Class<?> enclosingClass,
				Object enclosingInstance) {
			this.enclosingClass = enclosingClass;
			this.enclosingInstance = enclosingInstance;
		}

		@Override public boolean usesConstructor(Constructor<?> constructor) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			return parameterTypes.length == 1
					&& parameterTypes[0] == enclosingClass;
		}

		@Override public Constructor<?> getConstructor(Class<?> type)
				throws NoSuchMethodException {
			return type.getDeclaredConstructor(enclosingClass);
		}

		@Override public Object construct(Enhancer enhancer) {
			return enhancer.create(new Class<?>[] { enclosingClass },
					new Object[] { enclosingInstance });
		}
	}
}