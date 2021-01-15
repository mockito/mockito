/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.implementation.bytecode.StackSize;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.jar.asm.Type;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.SerializableMethod;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.plugins.MemberAccessor;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class MockMethodAdvice extends MockMethodDispatcher {

    private final WeakConcurrentMap<Object, MockMethodInterceptor> interceptors;
    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics;

    private final String identifier;

    private final SelfCallInfo selfCallInfo = new SelfCallInfo();
    private final MethodGraph.Compiler compiler = MethodGraph.Compiler.Default.forJavaHierarchy();
    private final WeakConcurrentMap<Class<?>, SoftReference<MethodGraph>> graphs =
            new WeakConcurrentMap.WithInlinedExpunction<Class<?>, SoftReference<MethodGraph>>();

    private final Predicate<Class<?>> isMockConstruction;
    private final ConstructionCallback onConstruction;

    public MockMethodAdvice(
            WeakConcurrentMap<Object, MockMethodInterceptor> interceptors,
            DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics,
            String identifier,
            Predicate<Class<?>> isMockConstruction,
            ConstructionCallback onConstruction) {
        this.interceptors = interceptors;
        this.mockedStatics = mockedStatics;
        this.onConstruction = onConstruction;
        this.identifier = identifier;
        this.isMockConstruction = isMockConstruction;
    }

    @SuppressWarnings("unused")
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    private static Callable<?> enter(
            @Identifier String identifier,
            @Advice.This Object mock,
            @Advice.Origin Method origin,
            @Advice.AllArguments Object[] arguments)
            throws Throwable {
        MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, mock);
        if (dispatcher == null
                || !dispatcher.isMocked(mock)
                || dispatcher.isOverridden(mock, origin)) {
            return null;
        } else {
            return dispatcher.handle(mock, origin, arguments);
        }
    }

    @SuppressWarnings({"unused", "UnusedAssignment"})
    @Advice.OnMethodExit
    private static void exit(
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
            @Advice.Enter Callable<?> mocked)
            throws Throwable {
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
            // This should not happen unless someone instrumented or manipulated exception stack
            // traces.
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
        return new ReturnValueWrapper(
                interceptor.doIntercept(
                        instance,
                        origin,
                        arguments,
                        realMethod,
                        new LocationImpl(new Throwable(), true)));
    }

    @Override
    public Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments)
            throws Throwable {
        Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
        if (interceptors == null || !interceptors.containsKey(type)) {
            return null;
        }
        return new ReturnValueWrapper(
                interceptors
                        .get(type)
                        .doIntercept(
                                type,
                                origin,
                                arguments,
                                new StaticMethodCall(selfCallInfo, type, origin, arguments),
                                new LocationImpl(new Throwable(), true)));
    }

    @Override
    public Object handleConstruction(
            Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames) {
        return onConstruction.apply(type, object, arguments, parameterTypeNames);
    }

    @Override
    public boolean isMock(Object instance) {
        // We need to exclude 'interceptors.target' explicitly to avoid a recursive check on whether
        // the map is a mock object what requires reading from the map.
        return instance != interceptors.target && interceptors.containsKey(instance);
    }

    @Override
    public boolean isMocked(Object instance) {
        return selfCallInfo.checkSelfCall(instance) && isMock(instance);
    }

    @Override
    public boolean isMockedStatic(Class<?> type) {
        if (!selfCallInfo.checkSelfCall(type)) {
            return false;
        }
        Map<Class<?>, ?> interceptors = mockedStatics.get();
        return interceptors != null && interceptors.containsKey(type);
    }

    @Override
    public boolean isOverridden(Object instance, Method origin) {
        SoftReference<MethodGraph> reference = graphs.get(instance.getClass());
        MethodGraph methodGraph = reference == null ? null : reference.get();
        if (methodGraph == null) {
            methodGraph = compiler.compile(new TypeDescription.ForLoadedType(instance.getClass()));
            graphs.put(instance.getClass(), new SoftReference<MethodGraph>(methodGraph));
        }
        MethodGraph.Node node =
                methodGraph.locate(
                        new MethodDescription.ForLoadedMethod(origin).asSignatureToken());
        return !node.getSort().isResolved()
                || !node.getRepresentative()
                        .asDefined()
                        .getDeclaringType()
                        .represents(origin.getDeclaringClass());
    }

    @Override
    public boolean isConstructorMock(Class<?> type) {
        return isMockConstruction.test(type);
    }

    private static class RealMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Method origin;

        private final MockWeakReference<Object> instanceRef;

        private final Object[] arguments;

        private RealMethodCall(
                SelfCallInfo selfCallInfo, Method origin, Object instance, Object[] arguments) {
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
            selfCallInfo.set(instanceRef.get());
            return tryInvoke(origin, instanceRef.get(), arguments);
        }
    }

    private static class SerializableRealMethodCall implements RealMethod {

        private final String identifier;

        private final SerializableMethod origin;

        private final MockReference<Object> instanceRef;

        private final Object[] arguments;

        private SerializableRealMethodCall(
                String identifier, Method origin, Object instance, Object[] arguments) {
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
            MockMethodDispatcher mockMethodDispatcher =
                    MockMethodDispatcher.get(identifier, instanceRef.get());
            if (!(mockMethodDispatcher instanceof MockMethodAdvice)) {
                throw new MockitoException("Unexpected dispatcher for advice-based super call");
            }
            Object previous =
                    ((MockMethodAdvice) mockMethodDispatcher)
                            .selfCallInfo.replace(instanceRef.get());
            try {
                return tryInvoke(method, instanceRef.get(), arguments);
            } finally {
                ((MockMethodAdvice) mockMethodDispatcher).selfCallInfo.set(previous);
            }
        }
    }

    private static class StaticMethodCall implements RealMethod {

        private final SelfCallInfo selfCallInfo;

        private final Class<?> type;

        private final Method origin;

        private final Object[] arguments;

        private StaticMethodCall(
                SelfCallInfo selfCallInfo, Class<?> type, Method origin, Object[] arguments) {
            this.selfCallInfo = selfCallInfo;
            this.type = type;
            this.origin = origin;
            this.arguments = arguments;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            selfCallInfo.set(type);
            return tryInvoke(origin, null, arguments);
        }
    }

    private static Object tryInvoke(Method origin, Object instance, Object[] arguments)
            throws Throwable {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        try {
            return accessor.invoke(origin, instance, arguments);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            new ConditionalStackTraceFilter()
                    .filter(
                            hideRecursiveCall(
                                    cause,
                                    new Throwable().getStackTrace().length,
                                    origin.getDeclaringClass()));
            throw cause;
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

        Object replace(Object value) {
            Object current = get();
            set(value);
            return current;
        }

        boolean checkSelfCall(Object value) {
            if (value == get()) {
                set(null);
                return false;
            } else {
                return true;
            }
        }
    }

    static class ConstructorShortcut
            implements AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {

        private final String identifier;

        ConstructorShortcut(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public MethodVisitor wrap(
                TypeDescription instrumentedType,
                MethodDescription instrumentedMethod,
                MethodVisitor methodVisitor,
                Implementation.Context implementationContext,
                TypePool typePool,
                int writerFlags,
                int readerFlags) {
            if (instrumentedMethod.isConstructor() && !instrumentedType.represents(Object.class)) {
                MethodList<MethodDescription.InDefinedShape> constructors =
                        instrumentedType
                                .getSuperClass()
                                .asErasure()
                                .getDeclaredMethods()
                                .filter(isConstructor().and(not(isPrivate())));
                int arguments = Integer.MAX_VALUE;
                boolean packagePrivate = true;
                MethodDescription.InDefinedShape current = null;
                for (MethodDescription.InDefinedShape constructor : constructors) {
                    // We are choosing the shortest constructor with regards to arguments.
                    // Yet, we prefer a non-package-private constructor since they require
                    // the super class to be on the same class loader.
                    if (constructor.getParameters().size() < arguments
                            && (packagePrivate || !constructor.isPackagePrivate())) {
                        arguments = constructor.getParameters().size();
                        packagePrivate = constructor.isPackagePrivate();
                        current = constructor;
                    }
                }
                if (current != null) {
                    final MethodDescription.InDefinedShape selected = current;
                    return new MethodVisitor(OpenedClassReader.ASM_API, methodVisitor) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            /*
                             * The byte code that is added to the start of the method is roughly equivalent to
                             * the following byte code for a hypothetical constructor of class Current:
                             *
                             * if (MockMethodDispatcher.isConstructorMock(<identifier>, Current.class) {
                             *   super(<default arguments>);
                             *   Current o = (Current) MockMethodDispatcher.handleConstruction(Current.class,
                             *       this,
                             *       new Object[] {argument1, argument2, ...},
                             *       new String[] {argumentType1, argumentType2, ...});
                             *   if (o != null) {
                             *     this.field = o.field; // for each declared field
                             *   }
                             *   return;
                             * }
                             *
                             * This avoids the invocation of the original constructor chain but fullfils the
                             * verifier requirement to invoke a super constructor.
                             */
                            Label label = new Label();
                            super.visitLdcInsn(identifier);
                            if (implementationContext
                                    .getClassFileVersion()
                                    .isAtLeast(ClassFileVersion.JAVA_V5)) {
                                super.visitLdcInsn(Type.getType(instrumentedType.getDescriptor()));
                            } else {
                                super.visitLdcInsn(instrumentedType.getName());
                                super.visitMethodInsn(
                                        Opcodes.INVOKESTATIC,
                                        Type.getInternalName(Class.class),
                                        "forName",
                                        Type.getMethodDescriptor(
                                                Type.getType(Class.class),
                                                Type.getType(String.class)),
                                        false);
                            }
                            super.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    Type.getInternalName(MockMethodDispatcher.class),
                                    "isConstructorMock",
                                    Type.getMethodDescriptor(
                                            Type.BOOLEAN_TYPE,
                                            Type.getType(String.class),
                                            Type.getType(Class.class)),
                                    false);
                            super.visitInsn(Opcodes.ICONST_0);
                            super.visitJumpInsn(Opcodes.IF_ICMPEQ, label);
                            super.visitVarInsn(Opcodes.ALOAD, 0);
                            for (TypeDescription type :
                                    selected.getParameters().asTypeList().asErasures()) {
                                if (type.represents(boolean.class)
                                        || type.represents(byte.class)
                                        || type.represents(short.class)
                                        || type.represents(char.class)
                                        || type.represents(int.class)) {
                                    super.visitInsn(Opcodes.ICONST_0);
                                } else if (type.represents(long.class)) {
                                    super.visitInsn(Opcodes.LCONST_0);
                                } else if (type.represents(float.class)) {
                                    super.visitInsn(Opcodes.FCONST_0);
                                } else if (type.represents(double.class)) {
                                    super.visitInsn(Opcodes.DCONST_0);
                                } else {
                                    super.visitInsn(Opcodes.ACONST_NULL);
                                }
                            }
                            super.visitMethodInsn(
                                    Opcodes.INVOKESPECIAL,
                                    selected.getDeclaringType().getInternalName(),
                                    selected.getInternalName(),
                                    selected.getDescriptor(),
                                    false);
                            super.visitLdcInsn(identifier);
                            if (implementationContext
                                    .getClassFileVersion()
                                    .isAtLeast(ClassFileVersion.JAVA_V5)) {
                                super.visitLdcInsn(Type.getType(instrumentedType.getDescriptor()));
                            } else {
                                super.visitLdcInsn(instrumentedType.getName());
                                super.visitMethodInsn(
                                        Opcodes.INVOKESTATIC,
                                        Type.getInternalName(Class.class),
                                        "forName",
                                        Type.getMethodDescriptor(
                                                Type.getType(Class.class),
                                                Type.getType(String.class)),
                                        false);
                            }
                            super.visitVarInsn(Opcodes.ALOAD, 0);
                            super.visitLdcInsn(instrumentedMethod.getParameters().size());
                            super.visitTypeInsn(
                                    Opcodes.ANEWARRAY, Type.getInternalName(Object.class));
                            int index = 0;
                            for (ParameterDescription parameter :
                                    instrumentedMethod.getParameters()) {
                                super.visitInsn(Opcodes.DUP);
                                super.visitLdcInsn(index++);
                                Type type =
                                        Type.getType(
                                                parameter.getType().asErasure().getDescriptor());
                                super.visitVarInsn(
                                        type.getOpcode(Opcodes.ILOAD), parameter.getOffset());
                                if (parameter.getType().isPrimitive()) {
                                    Type wrapper =
                                            Type.getType(
                                                    parameter
                                                            .getType()
                                                            .asErasure()
                                                            .asBoxed()
                                                            .getDescriptor());
                                    super.visitMethodInsn(
                                            Opcodes.INVOKESTATIC,
                                            wrapper.getInternalName(),
                                            "valueOf",
                                            Type.getMethodDescriptor(wrapper, type),
                                            false);
                                }
                                super.visitInsn(Opcodes.AASTORE);
                            }
                            index = 0;
                            super.visitLdcInsn(instrumentedMethod.getParameters().size());
                            super.visitTypeInsn(
                                    Opcodes.ANEWARRAY, Type.getInternalName(String.class));
                            for (TypeDescription typeDescription :
                                    instrumentedMethod.getParameters().asTypeList().asErasures()) {
                                super.visitInsn(Opcodes.DUP);
                                super.visitLdcInsn(index++);
                                super.visitLdcInsn(typeDescription.getName());
                                super.visitInsn(Opcodes.AASTORE);
                            }
                            super.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    Type.getInternalName(MockMethodDispatcher.class),
                                    "handleConstruction",
                                    Type.getMethodDescriptor(
                                            Type.getType(Object.class),
                                            Type.getType(String.class),
                                            Type.getType(Class.class),
                                            Type.getType(Object.class),
                                            Type.getType(Object[].class),
                                            Type.getType(String[].class)),
                                    false);
                            FieldList<FieldDescription.InDefinedShape> fields =
                                    instrumentedType.getDeclaredFields().filter(not(isStatic()));
                            super.visitTypeInsn(
                                    Opcodes.CHECKCAST, instrumentedType.getInternalName());
                            super.visitInsn(Opcodes.DUP);
                            Label noSpy = new Label();
                            super.visitJumpInsn(Opcodes.IFNULL, noSpy);
                            for (FieldDescription field : fields) {
                                super.visitInsn(Opcodes.DUP);
                                super.visitFieldInsn(
                                        Opcodes.GETFIELD,
                                        instrumentedType.getInternalName(),
                                        field.getInternalName(),
                                        field.getDescriptor());
                                super.visitVarInsn(Opcodes.ALOAD, 0);
                                super.visitInsn(
                                        field.getType().getStackSize() == StackSize.DOUBLE
                                                ? Opcodes.DUP_X2
                                                : Opcodes.DUP_X1);
                                super.visitInsn(Opcodes.POP);
                                super.visitFieldInsn(
                                        Opcodes.PUTFIELD,
                                        instrumentedType.getInternalName(),
                                        field.getInternalName(),
                                        field.getDescriptor());
                            }
                            super.visitLabel(noSpy);
                            if (implementationContext
                                    .getClassFileVersion()
                                    .isAtLeast(ClassFileVersion.JAVA_V6)) {
                                Object[] locals =
                                        toFrames(
                                                instrumentedType.getInternalName(),
                                                instrumentedMethod
                                                        .getParameters()
                                                        .asTypeList()
                                                        .asErasures());
                                super.visitFrame(
                                        Opcodes.F_FULL,
                                        locals.length,
                                        locals,
                                        1,
                                        new Object[] {instrumentedType.getInternalName()});
                            }
                            super.visitInsn(Opcodes.POP);
                            super.visitInsn(Opcodes.RETURN);
                            super.visitLabel(label);
                            if (implementationContext
                                    .getClassFileVersion()
                                    .isAtLeast(ClassFileVersion.JAVA_V6)) {
                                Object[] locals =
                                        toFrames(
                                                Opcodes.UNINITIALIZED_THIS,
                                                instrumentedMethod
                                                        .getParameters()
                                                        .asTypeList()
                                                        .asErasures());
                                super.visitFrame(
                                        Opcodes.F_FULL, locals.length, locals, 0, new Object[0]);
                            }
                        }

                        @Override
                        public void visitMaxs(int maxStack, int maxLocals) {
                            int prequel = Math.max(5, selected.getStackSize());
                            for (ParameterDescription parameter :
                                    instrumentedMethod.getParameters()) {
                                prequel =
                                        Math.max(
                                                prequel,
                                                6 + parameter.getType().getStackSize().getSize());
                                prequel = Math.max(prequel, 8);
                            }
                            super.visitMaxs(Math.max(maxStack, prequel), maxLocals);
                        }
                    };
                }
            }
            return methodVisitor;
        }

        private static Object[] toFrames(Object self, List<TypeDescription> types) {
            Object[] frames = new Object[1 + types.size()];
            frames[0] = self;
            int index = 0;
            for (TypeDescription type : types) {
                Object frame;
                if (type.represents(boolean.class)
                        || type.represents(byte.class)
                        || type.represents(short.class)
                        || type.represents(char.class)
                        || type.represents(int.class)) {
                    frame = Opcodes.INTEGER;
                } else if (type.represents(long.class)) {
                    frame = Opcodes.LONG;
                } else if (type.represents(float.class)) {
                    frame = Opcodes.FLOAT;
                } else if (type.represents(double.class)) {
                    frame = Opcodes.DOUBLE;
                } else {
                    frame = type.getInternalName();
                }
                frames[++index] = frame;
            }
            return frames;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Identifier {}

    static class ForHashCode {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static boolean enter(@Identifier String id, @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(id, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
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
        private static boolean enter(@Identifier String identifier, @Advice.This Object self) {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.get(identifier, self);
            return dispatcher != null && dispatcher.isMock(self);
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void enter(
                @Advice.This Object self,
                @Advice.Argument(0) Object other,
                @Advice.Return(readOnly = false) boolean equals,
                @Advice.Enter boolean skipped) {
            if (skipped) {
                equals = self == other;
            }
        }
    }

    static class ForStatic {

        @SuppressWarnings("unused")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        private static Callable<?> enter(
                @Identifier String identifier,
                @Advice.Origin Class<?> type,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] arguments)
                throws Throwable {
            MockMethodDispatcher dispatcher = MockMethodDispatcher.getStatic(identifier, type);
            if (dispatcher == null || !dispatcher.isMockedStatic(type)) {
                return null;
            } else {
                return dispatcher.handleStatic(type, origin, arguments);
            }
        }

        @SuppressWarnings({"unused", "UnusedAssignment"})
        @Advice.OnMethodExit
        private static void exit(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returned,
                @Advice.Enter Callable<?> mocked)
                throws Throwable {
            if (mocked != null) {
                returned = mocked.call();
            }
        }
    }

    public static class ForReadObject {

        @SuppressWarnings({"unused", "BanSerializableRead"})
        public static void doReadObject(
                @Identifier String identifier,
                @This MockAccess thiz,
                @Argument(0) ObjectInputStream objectInputStream)
                throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            MockMethodAdvice mockMethodAdvice =
                    (MockMethodAdvice) MockMethodDispatcher.get(identifier, thiz);
            if (mockMethodAdvice != null) {
                mockMethodAdvice.interceptors.put(thiz, thiz.getMockitoInterceptor());
            }
        }
    }
}
