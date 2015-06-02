package org.mockito.internal.creation.bytebuddy;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.DefaultCall;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

public class MockMethodInterceptor implements Serializable {

    private static final long serialVersionUID = 7152947254057253027L;

    private final InternalMockHandler handler;
    private final MockCreationSettings mockCreationSettings;

    private final ByteBuddyCrossClassLoaderSerializationSupport serializationSupport;

    public MockMethodInterceptor(InternalMockHandler handler, MockCreationSettings mockCreationSettings) {
        this.handler = handler;
        this.mockCreationSettings = mockCreationSettings;
        serializationSupport = new ByteBuddyCrossClassLoaderSerializationSupport();
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 3)
    public Object interceptSuperCallable(@This Object mock,
                                         @Origin(cache = true) Method invokedMethod,
                                         @AllArguments Object[] arguments,
                                         @SuperCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
        return doIntercept(
                mock,
                invokedMethod,
                arguments,
                new InterceptedInvocation.SuperMethod.FromCallable(superCall)
        );
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 2)
    public Object interceptDefaultCallable(@This Object mock,
                                           @Origin(cache = true) Method invokedMethod,
                                           @AllArguments Object[] arguments,
                                           @DefaultCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
        return doIntercept(
                mock,
                invokedMethod,
                arguments,
                new InterceptedInvocation.SuperMethod.FromCallable(superCall)
        );
    }

    @RuntimeType
    public Object interceptAbstract(@This Object mock,
                                    @Origin(cache = true) Method invokedMethod,
                                    @AllArguments Object[] arguments) throws Throwable {
        return doIntercept(
                mock,
                invokedMethod,
                arguments,
                InterceptedInvocation.SuperMethod.IsIllegal.INSTANCE
        );
    }

    private Object doIntercept(Object mock,
                               Method invokedMethod,
                               Object[] arguments,
                               InterceptedInvocation.SuperMethod superMethod) throws Throwable {
        return handler.handle(new InterceptedInvocation(
                mock,
                createMockitoMethod(invokedMethod),
                arguments,
                superMethod,
                SequenceNumber.next()
        ));
    }

    private MockitoMethod createMockitoMethod(Method method) {
        if (mockCreationSettings.isSerializable()) {
            return new SerializableMethod(method);
        } else {
            return new DelegatingMethod(method);
        }
    }

    public MockHandler getMockHandler() {
        return handler;
    }

    public ByteBuddyCrossClassLoaderSerializationSupport getSerializationSupport() {
        return serializationSupport;
    }

    public static class ForHashCode {
        public static int doIdentityHashCode(@This Object thiz) {
            return System.identityHashCode(thiz);
        }
    }

    public static class ForEquals {
        public static boolean doIdentityEquals(@This Object thiz, @Argument(0) Object other) {
            return thiz == other;
        }
    }

    public static class ForWriteReplace {
        public static Object doWriteReplace(@This MockAccess thiz) throws ObjectStreamException {
            return thiz.getMockitoInterceptor().getSerializationSupport().writeReplace(thiz);
        }
    }

    public static interface MockAccess {
        MockMethodInterceptor getMockitoInterceptor();
        void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
    }
}
