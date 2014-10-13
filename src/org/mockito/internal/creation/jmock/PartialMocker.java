package org.mockito.internal.creation.jmock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.CallbackFilter;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.NoOp;

abstract class PartialMocker {
    final<T> T partialMock(
        Class<T> toMock,
        Class<?>[] interfaces,
        MethodInterceptor mockInterceptor) {
      Enhancer enhancer = new Enhancer() {
        @SuppressWarnings({"rawtypes", "unchecked"}) // Cglib API is pre-generics
        @Override protected void filterConstructors(Class sc, List constructors) {
          super.filterConstructors(sc, constructors);
          Iterator<Constructor> it = constructors.iterator();
          while (it.hasNext()) {
        	  if (!usesConstructor(it.next())) {
        		  it.remove();
        	  }
          }
        }
      };
      enhancer.setSuperclass(toMock);
      enhancer.setInterfaces(interfaces);
      enhancer.setClassLoader(ClassImposterizer.getCombinedClassLoader(toMock, interfaces));
      enhancer.setCallbackTypes(new Class[] {NoOp.class, MethodInterceptor.class});
      enhancer.setCallbackFilter(new CallbackFilter() {
          @Override public int accept(Method method, List<Method> allMethods) {
            if (shouldBeOnMock(method)) {
              return 1;
            } else {
              return 0;
            }
          }
        });
      enhancer.setUseFactory(false);
      enhancer.setCallbacks(new Callback[] {NoOp.INSTANCE, mockInterceptor});
      ClassImposterizer.setNamingPolicy(enhancer, toMock);
      enhancer.setSerialVersionUID(43L);
      @SuppressWarnings("unchecked")  // toMock is Class<T>
	  T proxy = (T) createProxy(enhancer, new Callback[] {NoOp.INSTANCE, mockInterceptor});
      return proxy;
    }

    abstract Object createProxy(Enhancer enhancer, Callback[] callbacks);
	abstract boolean usesConstructor(Constructor<?> constructor);

    private static boolean shouldBeOnMock(Method method) {
      return Modifier.isAbstract(method.getModifiers()) && !method.isBridge();
    }
}
