/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

public class MockMethodAdvice extends MockMethodDispatcher {

    final WeakConcurrentMap<Object, MockMethodInterceptor> interceptors;

    private final String identifier;

    private final SelfCallInfo selfCallInfo = new SelfCallInfo();

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
        if (dispatcher == null || !dispatcher.isMocked(mock, origin)) {
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

    @Override
    public Callable<?> handle(Object instance, Method origin, Object[] arguments) throws Throwable {
        MockMethodInterceptor interceptor = interceptors.get(instance);
        if (interceptor == null) {
            return null;
        }
        InterceptedInvocation.SuperMethod superMethod;
        if (instance instanceof Serializable) {
            superMethod = new SerializableSuperMethodCall(identifier, origin, instance, arguments);
        } else {
            superMethod = new SuperMethodCall(selfCallInfo, origin, instance, arguments);
        }
        return new ReturnValueWrapper(interceptor.doIntercept(instance,
                origin,
                arguments,
                superMethod));
    }

    @Override
    public boolean isMock(Object instance) {
        return interceptors.containsKey(instance);
    }

    @Override
    public boolean isMocked(Object instance, Method origin) {
        return selfCallInfo.checkSuperCall(instance) && isMock(instance) && isNotOverridden(instance.getClass(), origin);
    }

    private static boolean isNotOverridden(Class<?> type, Method origin) {
        Class<?> currentType = type;
        do {
            try {
                return origin.equals(type.getDeclaredMethod(origin.getName(), origin.getParameterTypes()));
            } catch (NoSuchMethodException ignored) {
                currentType = currentType.getSuperclass();
            }
        } while (currentType != null);
        return true;
    }

    private static class SuperMethodCall implements InterceptedInvocation.SuperMethod {

        private final SelfCallInfo selfCallInfo;

        private final Method origin;

        private final Object instance;

        private final Object[] arguments;

        private SuperMethodCall(SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.origin = origin;
            this.instance = instance;
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
            selfCallInfo.set(instance);
            return tryInvoke(origin, instance, arguments);
        }

    }

    private static class SerializableSuperMethodCall implements InterceptedInvocation.SuperMethod {

        private final String identifier;

        private final SerializableMethod origin;

        private final Object instance;

        private final Object[] arguments;

        private SerializableSuperMethodCall(String identifier, Method origin, Object instance, Object[] arguments) {
            this.origin = new SerializableMethod(origin);
            this.identifier = identifier;
            this.instance = instance;
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
            MockMethodDispatcher mockMethodDispatcher = MockMethodDispatcher.get(identifier, instance);
            if (!(mockMethodDispatcher instanceof MockMethodAdvice)) {
                throw new MockitoException("Unexpected dispatcher for advice-based super call");
            }
            ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.set(instance);
            return tryInvoke(method, instance, arguments);
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

        boolean checkSuperCall(Object value) {
            Object current = get();
            if (current == value) {
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
