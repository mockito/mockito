/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MockMethodAdvice extends MockMethodDispatcher {

    private final WeakConcurrentMap<Object, MockMethodInterceptor> interceptors;

    private final String identifier;

    private final SelfCallInfo selfCallInfo = new SelfCallInfo();
    private final MethodGraph.Compiler compiler = MethodGraph.Compiler.Default.forJavaHierarchy();
    private final WeakConcurrentMap<Class<?>, SoftReference<MethodGraph>> graphs
        = new WeakConcurrentMap.WithInlinedExpunction<Class<?>, SoftReference<MethodGraph>>();

    public MockMethodAdvice(WeakConcurrentMap<Object, MockMethodInterceptor> interceptors, String identifier) {
        this.interceptors = interceptors;
        this.identifier = identifier;
    }

    @SuppressWarnings("unused")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    private static Callable<?> enter(@Identifier String identifier,
                                     @Advice.This Object mock,
                                     @Advice.Origin Method origin,
                                     @Advice.AllArguments Object[] arguments) throws Throwable {
        MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, mock);
        if (dispatcher == null || !dispatcher.isMocked(mock) || dispatcher.isOverridden(mock, origin)) {
            return null;
        } else {
            return dispatcher.handle(mock, origin, arguments);
        }
    }

    @SuppressWarnings({"unused", "UnusedAssignment"})
    @Advice.OnMethodExit
    private static void exit(@Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
                             @Advice.Enter Callable<?> mocked) throws Throwable {
        if (mocked != null) {
            returned = mocked.call();
        }
    }

    static Throwable hideRecursiveCall(Throwable throwable, int current, Class<?> targetType) {
        try {
            StackTraceElement[] stack = throwable.getStackTrace();
            int skip = 0;
            StackTraceElement next;
            do {
                next = stack[stack.length - current - ++skip];
            } while (!next.getClassName().equals(targetType.getName()));
            int top = stack.length - current - skip;
            StackTraceElement[] cleared = new StackTraceElement[stack.length - skip];
            System.arraycopy(stack, 0, cleared, 0, top);
            System.arraycopy(stack, top + skip, cleared, top, current);
            throwable.setStackTrace(cleared);
            return throwable;
        } catch (RuntimeException ignored) {
            // This should not happen unless someone instrumented or manipulated exception stack traces.
            return throwable;
        }
    }

    @Override
    public Callable<?> handle(Object instance, Method origin, Object[] arguments) throws Throwable {
        MockMethodInterceptor interceptor = interceptors.get(instance);
        if (interceptor == null) {
            return null;
        }
        RealMethod realMethod;
        if (instance instanceof Serializable) {
            realMethod = new SerializableRealMethodCall(identifier, origin, instance, arguments);
        } else {
            realMethod = new RealMethodCall(selfCallInfo, origin, instance, arguments);
        }
        Throwable t = new Throwable();
        t.setStackTrace(skipInlineMethodElement(t.getStackTrace()));
        return new ReturnValueWrapper(interceptor.doIntercept(instance,
                origin,
                arguments,
                realMethod,
                new LocationImpl(t)));
    }

    @Override
    public boolean isMock(Object instance) {
        // We need to exclude 'interceptors.target' explicitly to avoid a recursive check on whether
        // the map is a mock object what requires reading from the map.
        return instance != interceptors.target && interceptors.containsKey(instance);
    }

    @Override
    public boolean isMocked(Object instance) {
        return selfCallInfo.checkSuperCall(instance) && isMock(instance);
    }

    @Override
    public boolean isOverridden(Object instance, Method origin) {
        SoftReference<MethodGraph> reference = graphs.get(instance.getClass());
        MethodGraph methodGraph = reference == null ? null : reference.get();
        if (methodGraph == null) {
            methodGraph = compiler.compile(new TypeDescription.ForLoadedType(instance.getClass()));
            graphs.put(instance.getClass(), new SoftReference<MethodGraph>(methodGraph));
        }
        MethodGraph.Node node = methodGraph.locate(new MethodDescription.ForLoadedMethod(origin).asSignatureToken());
        return !node.getSort().isResolved() || !node.getRepresentative().asDefined().getDeclaringType().represents(origin.getDeclaringClass());
    }

    private static class RealMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Method origin;

        private final MockWeakReference<Object> instanceRef;

        private final Object[] arguments;

        private RealMethodCall(SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.origin = origin;
            this.instanceRef = new MockWeakReference<Object>(instance);
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            if (!Modifier.isPublic(origin.getDeclaringClass().getModifiers() & origin.getModifiers())) {
                origin.setAccessible(true);
            }
            selfCallInfo.set(instanceRef.get());
            return tryInvoke(origin, instanceRef.get(), arguments);
        }

    }

    private static class SerializableRealMethodCall implements RealMethod {

        private final String identifier;

        private final SerializableMethod origin;

        private final MockReference<Object> instanceRef;

        private final Object[] arguments;

        private SerializableRealMethodCall(String identifier, Method origin, Object instance, Object[] arguments) {
            this.origin = new SerializableMethod(origin);
            this.identifier = identifier;
            this.instanceRef = new MockWeakReference<Object>(instance);
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            Method method = origin.getJavaMethod();
            if (!Modifier.isPublic(method.getDeclaringClass().getModifiers() & method.getModifiers())) {
                method.setAccessible(true);
            }
            MockMethodDispatcher mockMethodDispatcher = MockMethodDispatcher.get(identifier, instanceRef.get());
            if (!(mockMethodDispatcher instanceof MockMethodAdvice)) {
                throw new MockitoException("Unexpected dispatcher for advice-based super call");
            }
            Object previous = ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.replace(instanceRef.get());
            try {
                return tryInvoke(method, instanceRef.get(), arguments);
            } finally {
                ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.set(previous);
            }
        }
    }

    private static Object tryInvoke(Method origin, Object instance, Object[] arguments) throws Throwable {
        try {
            return origin.invoke(instance, arguments);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            new ConditionalStackTraceFilter().filter(hideRecursiveCall(cause, new Throwable().getStackTrace().length, origin.getDeclaringClass()));
            throw cause;
        }
    }

    // With inline mocking, mocks for concrete classes are not subclassed, so elements of the stubbing methods are not filtered out.
    // Therefore, if the method is inlined, skip the element.
    private static StackTraceElement[] skipInlineMethodElement(StackTraceElement[] elements) {
        List<StackTraceElement> list = new ArrayList<StackTraceElement>(elements.length);
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            list.add(element);
            if (element.getClassName().equals(MockMethodAdvice.class.getName()) && element.getMethodName().equals("handle")) {
                // If the current element is MockMethodAdvice#handle(), the next is assumed to be an inlined method.
                i++;
            }
        }
        return list.toArray(new StackTraceElement[list.size()]);
    }

    private static class ReturnValueWrapper implements Callable<Object> {

        private final Object returned;

        private ReturnValueWrapper(Object returned) {
            this.returned = returned;
        }

        @Override
        public Object call() {
            return returned;
        }
    }

    private static class SelfCallInfo extends ThreadLocal<Object> {

        Object replace(Object value) {
            Object current = get();
            set(value);
            return current;
        }

        boolean checkSuperCall(Object value) {
            if (value == get()) {
                set(null);
                return false;
            } else {
                return true;
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Identifier {

    }

    static class ForHashCode {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String id,
                                     @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(id, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(@Advice.This Object self,
                                  @Advice.Return(readOnly = false) int hashCode,
                                  @Advice.Enter boolean skipped) {
            if (skipped) {
                hashCode = System.identityHashCode(self);
            }
        }
    }

    static class ForEquals {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String identifier,
                                     @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(@Advice.This Object self,
                                  @Advice.Argument(0) Object other,
                                  @Advice.Return(readOnly = false) boolean equals,
                                  @Advice.Enter boolean skipped) {
            if (skipped) {
                equals = self == other;
            }
        }
    }

    public static class ForReadObject {

        @SuppressWarnings("unused")
        public static void doReadObject(@Identifier String identifier,
                                        @This MockAccess thiz,
                                        @Argument(0) ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            MockMethodAdvice mockMethodAdvice = (MockMethodAdvice) MockMethodDispatcher.get(identifier, thiz);
            if (mockMethodAdvice != null) {
                mockMethodAdvice.interceptors.put(thiz, thiz.getMockitoInterceptor());
            }
        }
    }
}
