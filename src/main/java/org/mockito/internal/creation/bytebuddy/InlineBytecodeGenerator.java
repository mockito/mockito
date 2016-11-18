/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.RandomString;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.internal.util.concurrent.WeakConcurrentSet;
import org.mockito.mock.SerializableMode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue.OfConstant.of;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class InlineBytecodeGenerator implements BytecodeGenerator, ClassFileTransformer {

    @SuppressWarnings("unchecked")
    static final Set<Class<?>> EXCLUDES = new HashSet<Class<?>>(Arrays.asList(Class.class,
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class));

    private final Instrumentation instrumentation;

    private final ByteBuddy byteBuddy;

    private final WeakConcurrentSet<Class<?>> mocked;

    private final String identifier;

    private final MockMethodAdvice advice;

    private final BytecodeGenerator subclassEngine;

    public InlineBytecodeGenerator(Instrumentation instrumentation, WeakConcurrentMap<Object, MockMethodInterceptor> mocks) {
        this.instrumentation = instrumentation;
        byteBuddy = new ByteBuddy()
                .with(TypeValidation.DISABLED)
                .with(Implementation.Context.Disabled.Factory.INSTANCE);
        mocked = new WeakConcurrentSet<Class<?>>(WeakConcurrentSet.Cleaner.INLINE);
        identifier = RandomString.make();
        advice = new MockMethodAdvice(mocks, identifier);
        subclassEngine = new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(to(MockMethodAdvice.ForReadObject.class)
                .appendParameterBinder(of(MockMethodAdvice.Identifier.class, identifier)), isAbstract().or(isNative())), false);
        MockMethodDispatcher.set(identifier, advice);
        instrumentation.addTransformer(this, true);
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean subclassingRequired = !features.interfaces.isEmpty()
                || features.serializableMode != SerializableMode.NONE
                || Modifier.isAbstract(features.mockedType.getModifiers());

        checkSupportedCombination(subclassingRequired, features);

        synchronized (this) {
            triggerRetransformation(features);
        }

        return subclassingRequired ?
                subclassEngine.mockClass(features) :
                features.mockedType;
    }

    private <T> void triggerRetransformation(MockFeatures<T> features) {
        Set<Class<?>> types = new HashSet<Class<?>>();
        Class<?> type = features.mockedType;
        do {
            if (mocked.add(type)) {
                types.add(type);
                addInterfaces(types, type.getInterfaces());
            }
            type = type.getSuperclass();
        } while (type != null);
        if (!types.isEmpty()) {
            try {
                instrumentation.retransformClasses(types.toArray(new Class<?>[types.size()]));
            } catch (UnmodifiableClassException exception) {
                for (Class<?> failed : types) {
                    mocked.remove(failed);
                }
                throw new MockitoException("Could not modify all classes " + types, exception);
            }
        }
    }

    private <T> void checkSupportedCombination(boolean subclassingRequired, MockFeatures<T> features) {
        if (subclassingRequired
                && !features.mockedType.isArray()
                && !features.mockedType.isPrimitive()
                && Modifier.isFinal(features.mockedType.getModifiers())) {
            throw new MockitoException("Unsupported settings with this type '" + features.mockedType.getName() + "'");
        }
    }

    private void addInterfaces(Set<Class<?>> types, Class<?>[] interfaces) {
        for (Class<?> type : interfaces) {
            if (mocked.add(type)) {
                types.add(type);
                addInterfaces(types, type.getInterfaces());
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classBeingRedefined == null
                || !mocked.contains(classBeingRedefined)
                || EXCLUDES.contains(classBeingRedefined)) {
            return null;
        } else {
            try {
                return byteBuddy.redefine(classBeingRedefined, ClassFileLocator.Simple.of(classBeingRedefined.getName(), classfileBuffer))
                        // Note: The VM erases parameter meta data from the provided class file (bug). We just add this information manually.
                        .visit(new ParameterWritingVisitorWrapper(classBeingRedefined))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.class).on(isVirtual()
                                        .and(not(isBridge().or(isHashCode()).or(isEquals()).or(isDefaultFinalizer())))
                                        .and(not(isDeclaredBy(nameStartsWith("java.")).<MethodDescription>and(isPackagePrivate())))))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.ForHashCode.class).on(isHashCode()))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.ForEquals.class).on(isEquals()))
                        .make()
                        .getBytes();
            } catch (Throwable throwable) {
                return null;
            }
        }
    }

    private static class ParameterWritingVisitorWrapper extends AsmVisitorWrapper.AbstractBase {

        private final Class<?> type;

        private ParameterWritingVisitorWrapper(Class<?> type) {
            this.type = type;
        }

        @Override
        public ClassVisitor wrap(TypeDescription instrumentedType,
                                 ClassVisitor classVisitor,
                                 Implementation.Context implementationContext,
                                 TypePool typePool,
                                 int writerFlags,
                                 int readerFlags) {
            return implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V8)
                    ? new ParameterAddingClassVisitor(classVisitor, new TypeDescription.ForLoadedType(type))
                    : classVisitor;
        }

        private static class ParameterAddingClassVisitor extends ClassVisitor {

            private final TypeDescription typeDescription;

            private ParameterAddingClassVisitor(ClassVisitor cv, TypeDescription typeDescription) {
                super(Opcodes.ASM5, cv);
                this.typeDescription = typeDescription;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                MethodList<?> methodList = typeDescription.getDeclaredMethods().filter((name.equals(MethodDescription.CONSTRUCTOR_INTERNAL_NAME)
                        ? isConstructor()
                        : ElementMatchers.<MethodDescription>named(name)).and(hasDescriptor(desc)));
                if (methodList.size() == 1 && methodList.getOnly().getParameters().hasExplicitMetaData()) {
                    for (ParameterDescription parameterDescription : methodList.getOnly().getParameters()) {
                        methodVisitor.visitParameter(parameterDescription.getName(), parameterDescription.getModifiers());
                    }
                    return new MethodParameterStrippingMethodVisitor(methodVisitor);
                } else {
                    return methodVisitor;
                }
            }
        }

        private static class MethodParameterStrippingMethodVisitor extends MethodVisitor {

            public MethodParameterStrippingMethodVisitor(MethodVisitor mv) {
                super(Opcodes.ASM5, mv);
            }

            @Override
            public void visitParameter(String name, int access) {
                // suppress to avoid additional writing of the parameter if retained.
            }
        }
    }
}
