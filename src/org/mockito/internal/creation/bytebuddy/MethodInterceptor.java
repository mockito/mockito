package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.instrumentation.method.bytecode.bind.annotation.*;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.AcrossJVMSerializationFeature;
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

public class MethodInterceptor implements Serializable {

    private static final long serialVersionUID = 7152947254057253027L;

    private final InternalMockHandler handler;
    private final MockCreationSettings mockCreationSettings;

    private final AcrossJVMSerializationFeature acrossJVMSerializationFeature;

    public MethodInterceptor(InternalMockHandler handler,
                             MockCreationSettings mockCreationSettings) {
        this.handler = handler;
        this.mockCreationSettings = mockCreationSettings;
        acrossJVMSerializationFeature = new AcrossJVMSerializationFeature();
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 3)
    public Object interceptSuperCallable(@This Object mock,
                                         @Origin Method method,
                                         @AllArguments Object[] arguments,
                                         @SuperCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
        return doIntercept(mock, method, arguments, new InterceptedInvocation.SuperMethod.FromCallable(superCall));
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 2)
    public Object interceptDefaultCallable(@This Object mock,
                                           @Origin Method method,
                                           @AllArguments Object[] arguments,
                                           @DefaultCall(serializableProxy = true) Callable<?> superCall) throws Throwable {
        return doIntercept(mock, method, arguments, new InterceptedInvocation.SuperMethod.FromCallable(superCall));
    }

    @RuntimeType
    public Object interceptAbstract(@This Object mock,
                                    @Origin Method method,
                                    @AllArguments Object[] arguments) throws Throwable {
        return doIntercept(mock, method, arguments, InterceptedInvocation.SuperMethod.IsIllegal.INSTANCE);
    }

    private Object doIntercept(Object mock,
                               Method method,
                               Object[] arguments,
                               InterceptedInvocation.SuperMethod superMethod) throws Throwable {
        return handler.handle(new InterceptedInvocation(mock,
                createMockitoMethod(method),
                arguments,
                superMethod,
                SequenceNumber.next()));
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

    public AcrossJVMSerializationFeature getAcrossJVMSerializationFeature() {
        return acrossJVMSerializationFeature;
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

        public static Object doWriteReplace(@This Access thiz) throws ObjectStreamException {
            return thiz.getMockitoInterceptor().getAcrossJVMSerializationFeature().writeReplace(thiz);
        }
    }

    public static interface Access {

        MethodInterceptor getMockitoInterceptor();

        void setMockitoInterceptor(MethodInterceptor methodInterceptor);
    }
}
