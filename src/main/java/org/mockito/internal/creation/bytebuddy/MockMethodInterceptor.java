/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.invocation.DefaultInvocationFactory.createInvocation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.invocation.Location;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

public class MockMethodInterceptor implements Serializable {

    private static final long serialVersionUID = 7152947254057253027L;

    final MockHandler handler;

    private final MockCreationSettings mockCreationSettings;

    private final ByteBuddyCrossClassLoaderSerializationSupport serializationSupport;

    private transient ThreadLocal<Object> weakReferenceHatch = new ThreadLocal<>();

    public MockMethodInterceptor(MockHandler handler, MockCreationSettings mockCreationSettings) {
        this.handler = handler;
        this.mockCreationSettings = mockCreationSettings;
        serializationSupport = new ByteBuddyCrossClassLoaderSerializationSupport();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        weakReferenceHatch = new ThreadLocal<>();
    }

    Object doIntercept(Object mock, Method invokedMethod, Object[] arguments, RealMethod realMethod)
            throws Throwable {
        return doIntercept(mock, invokedMethod, arguments, realMethod, new LocationImpl());
    }

    Object doIntercept(
            Object mock,
            Method invokedMethod,
            Object[] arguments,
            RealMethod realMethod,
            Location location)
            throws Throwable {
        // If the currently dispatched method is used in a hot path, typically a tight loop and if
        // the mock is not used after the currently dispatched method, the JVM might attempt a
        // garbage collection of the mock instance even before the execution of the current
        // method is completed. Since we only reference the mock weakly from hereon after to avoid
        // leaking the instance, it might therefore be garbage collected before the
        // handler.handle(...) method completes. Since the handler method expects the mock to be
        // present while a method call onto the mock is dispatched, this can lead to the problem
        // described in GitHub #1802.
        //
        // To avoid this problem, we distract the JVM JIT by escaping the mock instance to a thread
        // local field for the duration of the handler's dispatch.
        //
        // When dropping support for Java 8, instead of this hatch we should use an explicit fence
        // https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Reference.html#reachabilityFence-java.lang.Object-
        weakReferenceHatch.set(mock);
        try {
            return handler.handle(
                    createInvocation(
                            mock,
                            invokedMethod,
                            arguments,
                            realMethod,
                            mockCreationSettings,
                            location));
        } finally {
            weakReferenceHatch.remove();
        }
    }

    public MockHandler getMockHandler() {
        return handler;
    }

    public ByteBuddyCrossClassLoaderSerializationSupport getSerializationSupport() {
        return serializationSupport;
    }

    public static class ForHashCode {

        @SuppressWarnings("unused")
        public static int doIdentityHashCode(@This Object thiz) {
            return System.identityHashCode(thiz);
        }
    }

    public static class ForEquals {

        @SuppressWarnings("unused")
        public static boolean doIdentityEquals(@This Object thiz, @Argument(0) Object other) {
            return thiz == other;
        }
    }

    public static class ForWriteReplace {

        public static Object doWriteReplace(@This MockAccess thiz) throws ObjectStreamException {
            return thiz.getMockitoInterceptor().getSerializationSupport().writeReplace(thiz);
        }
    }

    public static class DispatcherDefaultingToRealMethod {

        @SuppressWarnings("unused")
        @RuntimeType
        @BindingPriority(BindingPriority.DEFAULT * 2)
        public static Object interceptSuperCallable(
                @This Object mock,
                @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor,
                @Origin Method invokedMethod,
                @AllArguments Object[] arguments,
                @SuperCall(serializableProxy = true) Callable<?> superCall)
                throws Throwable {
            if (interceptor == null) {
                return superCall.call();
            }
            return interceptor.doIntercept(
                    mock, invokedMethod, arguments, new RealMethod.FromCallable(superCall));
        }

        @SuppressWarnings("unused")
        @RuntimeType
        public static Object interceptAbstract(
                @This Object mock,
                @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor,
                @StubValue Object stubValue,
                @Origin Method invokedMethod,
                @AllArguments Object[] arguments)
                throws Throwable {
            if (interceptor == null) {
                return stubValue;
            }
            return interceptor.doIntercept(
                    mock, invokedMethod, arguments, RealMethod.IsIllegal.INSTANCE);
        }
    }
}
