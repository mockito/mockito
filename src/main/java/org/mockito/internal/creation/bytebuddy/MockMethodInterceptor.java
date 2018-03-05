/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static org.mockito.internal.invocation.DefaultInvocationFactory.createInvocation;

public class MockMethodInterceptor implements Serializable {

    private static final long serialVersionUID = 7152947254057253027L;

    final MockHandler handler;

    private final MockCreationSettings mockCreationSettings;

    private final ByteBuddyCrossClassLoaderSerializationSupport serializationSupport;

    public MockMethodInterceptor(MockHandler handler, MockCreationSettings mockCreationSettings) {
        this.handler = handler;
        this.mockCreationSettings = mockCreationSettings;
        serializationSupport = new ByteBuddyCrossClassLoaderSerializationSupport();
    }

    Object doIntercept(Object mock,
                       Method invokedMethod,
                       Object[] arguments,
                       RealMethod realMethod) throws Throwable {
        return doIntercept(
                mock,
                invokedMethod,
                arguments,
            realMethod,
                new LocationImpl()
        );
    }

    Object doIntercept(Object mock,
                       Method invokedMethod,
                       Object[] arguments,
                       RealMethod realMethod,
                       Location location) throws Throwable {
        return handler.handle(createInvocation(mock, invokedMethod, arguments, realMethod, mockCreationSettings, location));
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
        public static Object interceptSuperCallable(@This Object mock,
                                                    @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor,
                                                    @Origin Method invokedMethod,
                                                    @AllArguments Object[] arguments,
                                                    @SuperCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
            if (interceptor == null) {
                return superCall.call();
            }
            return interceptor.doIntercept(
                    mock,
                    invokedMethod,
                    arguments,
                    new RealMethod.FromCallable(superCall)
            );
        }

        @SuppressWarnings("unused")
        @RuntimeType
        public static Object interceptAbstract(@This Object mock,
                                               @FieldValue("mockitoInterceptor") MockMethodInterceptor interceptor,
                                               @StubValue Object stubValue,
                                               @Origin Method invokedMethod,
                                               @AllArguments Object[] arguments) throws Throwable {
            if (interceptor == null) {
                return stubValue;
            }
            return interceptor.doIntercept(
                    mock,
                    invokedMethod,
                    arguments,
                    RealMethod.IsIllegal.INSTANCE
            );
        }
    }
}
