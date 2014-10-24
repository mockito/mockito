package org.mockito.internal.creation.cglib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
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
import org.mockito.internal.creation.util.SearchingClassLoader;

/** Responsible for creating proxy class and proxy instance using cglib. */
public abstract class ProxyMaker {
	private static final CallbackFilter IGNORE_BRIDGE_METHODS = new CallbackFilter() {
		@Override public int accept(Method method) {
			return method.isBridge() ? 1 : 0;
		}
	};

	private static final NamingPolicy NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES =
			new MockitoNamingPolicy() {
				@Override public String getClassName(
						String prefix, String source, Object key, Predicate names) {
					return "codegen." + super.getClassName(prefix, source, key, names);
				}
			};

	/**
	 * Makes proxy class for {@code clazz} that in addition implements {@code interfaces}.
	 */
	public static Class<? extends Factory> makeProxyClass(Class<?> clazz, Class<?>... interfaces) {
		if (clazz == Object.class) {
			clazz = ClassWithSuperclassToWorkAroundCglibBug.class;
		}

		Enhancer enhancer = new Enhancer() {
			@SuppressWarnings("rawtypes")
			@Override protected void filterConstructors(Class sc, List constructors) {
				// Don't filter
			}
		};
		configureMock(enhancer, clazz, interfaces);

		try {
			@SuppressWarnings("unchecked")
			// because of setUseFactory(true)
			Class<? extends Factory> proxyClass = enhancer.createClass();
			return proxyClass;
		} catch (CodeGenerationException e) {
			if (Modifier.isPrivate(clazz.getModifiers())) {
				throw new MockitoException(
						"\n"
						+ "Mockito cannot mock this class: "
						+ clazz
						+ ".\n"
						+ "Most likely it is a private class that is not visible by Mockito");
			}
			throw new MockitoException(
					"\n"
					+ "Mockito cannot mock this class: "
					+ clazz
					+ "\n"
					+ "Mockito can only mock visible & non-final classes."
					+ "\n"
					+ "If you're not sure why you're getting this error, please report to the mailing list.",
					e);
		}
	}

	/**
	 * Makes a new instance for {@code clazz} that implements {@code interfaces}
	 * using {@code interceptor}.
	 */
	final <T> T make(Class<T> clazz, Class<?>[] interfaces, MethodInterceptor interceptor) {
		Enhancer enhancer = new Enhancer() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			// Cglib API is pre-generics
			@Override protected void filterConstructors(Class sc, List constructors) {
				Iterator<Constructor> it = constructors.iterator();
				while (it.hasNext()) {
					if (!usesConstructor(it.next())) {
						it.remove();
					}
				}
			}
		};
		configureMock(enhancer, clazz, interfaces);
		@SuppressWarnings("unchecked")
		// clazz is Class<T>
		T proxy = (T) createProxy(enhancer, interceptor, SerializableNoOp.INSTANCE);
		return proxy;
	}

	/**
	 * Override this method to configure how the proxy instance should be constructed.
	 */
	abstract Object createProxy(Enhancer enhancer, Callback... callbacks);

	/**
	 * Returns true if {@code constructor} should be used to construct the proxy.
	 */
	abstract boolean usesConstructor(Constructor<?> constructor);

	private static void configureMock(Enhancer enhancer, Class<?> clazz, Class<?>... interfaces) {
		if (clazz.isInterface()) {
			enhancer.setSuperclass(Object.class);
			enhancer.setInterfaces(prepend(clazz, interfaces));
		} else {
			enhancer.setSuperclass(clazz);
			enhancer.setInterfaces(interfaces);
		}
		enhancer.setUseFactory(true); // We need Factory to check if an instance is a mock.
		enhancer.setCallbackTypes(new Class[] { MethodInterceptor.class, NoOp.class });
		enhancer.setCallbackFilter(ProxyMaker.IGNORE_BRIDGE_METHODS);
		enhancer.setClassLoader(getCombinedClassLoader(clazz, interfaces));
		setNamingPolicy(enhancer, clazz);
		enhancer.setSerialVersionUID(42L);
	}

	private static ClassLoader getCombinedClassLoader(Class<?> mockedType, Class<?>... interfaces) {
		return SearchingClassLoader.combineLoadersOf(prepend(mockedType, interfaces));
	}

	private static void setNamingPolicy(Enhancer enhancer, Class<?> mockedType) {
		if (mockedType.getSigners() != null) {
			enhancer.setNamingPolicy(NAMING_POLICY_THAT_ALLOWS_IMPOSTERISATION_OF_CLASSES_IN_SIGNED_PACKAGES);
		} else {
			enhancer.setNamingPolicy(MockitoNamingPolicy.INSTANCE);
		}
	}

	private static Class<?>[] prepend(Class<?> first, Class<?>... rest) {
		Class<?>[] all = new Class<?>[rest.length + 1];
		all[0] = first;
		System.arraycopy(rest, 0, all, 1, rest.length);
		return all;
	}

	public static class ClassWithSuperclassToWorkAroundCglibBug {}
}
