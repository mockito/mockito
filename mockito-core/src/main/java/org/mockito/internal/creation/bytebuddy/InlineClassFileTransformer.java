/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.hasDescriptor;
import static net.bytebuddy.matcher.ElementMatchers.isBridge;
import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.isDefaultFinalizer;
import static net.bytebuddy.matcher.ElementMatchers.isEquals;
import static net.bytebuddy.matcher.ElementMatchers.isHashCode;
import static net.bytebuddy.matcher.ElementMatchers.isPackagePrivate;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.isSynthetic;
import static net.bytebuddy.matcher.ElementMatchers.isVirtual;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription.InDefinedShape;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;

public class InlineClassFileTransformer implements ClassFileTransformer {

    public static final boolean MOCKITO_AOT = Boolean.getBoolean("org.mockito.aot");

    private final ByteBuddy byteBuddy;
    private final AsmVisitorWrapper mockTransformer;

    protected volatile Throwable lastException;

    static final Set<Class<?>> EXCLUDES =
            new HashSet<>(
                    Arrays.asList(
                            Class.class,
                            Boolean.class,
                            Byte.class,
                            Short.class,
                            Character.class,
                            Integer.class,
                            Long.class,
                            Float.class,
                            Double.class,
                            String.class));

    public InlineClassFileTransformer(String identifier) {
        byteBuddy =
                new ByteBuddy()
                        .with(TypeValidation.DISABLED)
                        .with(Implementation.Context.Disabled.Factory.INSTANCE)
                        .with(MethodGraph.Compiler.ForDeclaredMethods.INSTANCE)
                        .ignore(isSynthetic().and(not(isConstructor())).or(isDefaultFinalizer()));
        mockTransformer =
                new AsmVisitorWrapper.ForDeclaredMethods()
                        .method(
                                isVirtual()
                                        .and(
                                                not(
                                                        isBridge()
                                                                .or(isHashCode())
                                                                .or(isEquals())
                                                                .or(isDefaultFinalizer())))
                                        .and(
                                                not(isDeclaredBy(nameStartsWith("java."))
                                                                .<MethodDescription>and(
                                                                        isPackagePrivate()))
                                                        .and(
                                                                not(
                                                                        BytecodeGenerator
                                                                                .isGroovyMethod(
                                                                                        true)))),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.class))
                        .method(
                                isStatic().and(not(BytecodeGenerator.isGroovyMethod(true))),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForStatic.class))
                        .constructor(any(), new MockMethodAdvice.ConstructorShortcut(identifier))
                        .method(
                                isHashCode(),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForHashCode.class))
                        .method(
                                isEquals(),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForEquals.class));
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {
        if (classBeingRedefined == null || EXCLUDES.contains(classBeingRedefined)) {
            return null;
        } else {
            return doTransform(
                    TypeDescription.ForLoadedType.of(classBeingRedefined), classfileBuffer, false);
        }
    }

    public byte[] transform(String className, byte[] classfileBuffer) {
        TypePool typePool =
                TypePool.Default.WithLazyResolution.of(
                        ClassFileLocator.Simple.of(className, classfileBuffer));
        return doTransform(typePool.describe(className).resolve(), classfileBuffer, true);
    }

    private byte[] doTransform(
            TypeDescription classBeingRedefined, byte[] classfileBuffer, boolean annotate) {
        try {
            return byteBuddy
                    .redefine(
                            classBeingRedefined,
                            //        new ClassFileLocator.Compound(
                            ClassFileLocator.Simple.of(
                                    classBeingRedefined.getName(), classfileBuffer)
                            //            ,ClassFileLocator.ForClassLoader.ofSystemLoader()
                            //        )
                            )
                    .annotateType(
                            annotate
                                    ? Collections.singletonList(InlineMockMarker.DESCRIPTION)
                                    : Collections.emptyList())
                    // Note: The VM erases parameter meta data from the provided class file
                    // (bug). We just add this information manually.
                    .visit(new ParameterWritingVisitorWrapper(classBeingRedefined))
                    .visit(mockTransformer)
                    .make()
                    .getBytes();
        } catch (Throwable throwable) {
            lastException = throwable;
            return null;
        }
    }

    private static class ParameterWritingVisitorWrapper extends AsmVisitorWrapper.AbstractBase {

        private final TypeDescription typeDescription;

        private ParameterWritingVisitorWrapper(TypeDescription typeDescription) {
            this.typeDescription = typeDescription;
        }

        @Override
        public ClassVisitor wrap(
                TypeDescription instrumentedType,
                ClassVisitor classVisitor,
                Implementation.Context implementationContext,
                TypePool typePool,
                FieldList<InDefinedShape> fields,
                MethodList<?> methods,
                int writerFlags,
                int readerFlags) {
            return implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V8)
                    ? new ParameterAddingClassVisitor(classVisitor, typeDescription)
                    : classVisitor;
        }

        private static class ParameterAddingClassVisitor extends ClassVisitor {

            private final TypeDescription typeDescription;

            private ParameterAddingClassVisitor(ClassVisitor cv, TypeDescription typeDescription) {
                super(OpenedClassReader.ASM_API, cv);
                this.typeDescription = typeDescription;
            }

            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor =
                        super.visitMethod(access, name, desc, signature, exceptions);
                MethodList<?> methodList =
                        typeDescription
                                .getDeclaredMethods()
                                .filter(
                                        (name.equals(MethodDescription.CONSTRUCTOR_INTERNAL_NAME)
                                                        ? isConstructor()
                                                        : ElementMatchers.<MethodDescription>named(
                                                                name))
                                                .and(hasDescriptor(desc)));
                if (methodList.size() == 1
                        && methodList.getOnly().getParameters().hasExplicitMetaData()) {
                    for (ParameterDescription parameterDescription :
                            methodList.getOnly().getParameters()) {
                        methodVisitor.visitParameter(
                                parameterDescription.getName(),
                                parameterDescription.getModifiers());
                    }
                    return new MethodParameterStrippingMethodVisitor(methodVisitor);
                } else {
                    return methodVisitor;
                }
            }
        }

        private static class MethodParameterStrippingMethodVisitor extends MethodVisitor {

            public MethodParameterStrippingMethodVisitor(MethodVisitor mv) {
                super(OpenedClassReader.ASM_API, mv);
            }

            @Override
            public void visitParameter(String name, int access) {
                // suppress to avoid additional writing of the parameter if retained.
            }
        }
    }
}
