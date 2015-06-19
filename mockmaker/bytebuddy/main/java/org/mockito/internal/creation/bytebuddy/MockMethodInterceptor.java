package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.*;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

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

    public interface MockAccess {
        MockMethodInterceptor getMockitoInterceptor();
        void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
    }

    public static class InterceptionDispatcher {
        public interface FieldGetter<T> {
            T getValue();
        }
        public interface FieldSetter<T> {
            void setValue(T value);
        }

        @RuntimeType
        @BindingPriority(BindingPriority.DEFAULT * 2)
        public static Object interceptSuperCallable(@This Object mock,
                                                    @FieldProxy("mockitoInterceptor") FieldGetter<MockMethodInterceptor> fieldGetter,
                                                    @Origin Method invokedMethod,
                                                    @AllArguments Object[] arguments,
                                                    @SuperCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
            MockMethodInterceptor interceptor = fieldGetter.getValue();
            if (interceptor == null) {
                return null;
            }
            return interceptor.doIntercept(
                    mock,
                    invokedMethod,
                    arguments,
                    new InterceptedInvocation.SuperMethod.FromCallable(superCall)
            );
        }

        @RuntimeType
        public static Object interceptAbstract(@This Object mock,
                                               @FieldProxy("mockitoInterceptor") FieldGetter<MockMethodInterceptor> fieldGetter,
                                               @Origin(cache = true) Method invokedMethod,
                                               @AllArguments Object[] arguments) throws Throwable {
            MockMethodInterceptor interceptor = fieldGetter.getValue();
            if (interceptor == null) {
                return null;
            }
            return interceptor.doIntercept(
                    mock,
                    invokedMethod,
                    arguments,
                    InterceptedInvocation.SuperMethod.IsIllegal.INSTANCE
            );
        }
    }
}
