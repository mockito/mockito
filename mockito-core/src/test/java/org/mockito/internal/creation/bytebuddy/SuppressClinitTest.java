/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.utility.OpenedClassReader;
import org.junit.Test;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MockMaker;

public class SuppressClinitTest {

    @Test
    public void suppress_clinit_removes_static_initializer() throws Exception {
        // Generate a class with a static initializer that sets a static field
        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name("org.mockito.test.ClassWithClinit")
                        .defineField(
                                "INITIALIZED", boolean.class, Visibility.PUBLIC, Ownership.STATIC)
                        .invokable(net.bytebuddy.matcher.ElementMatchers.isTypeInitializer())
                        .intercept(FixedValue.value(true))
                        .make()
                        .getBytes();

        // Verify original has a <clinit>
        assertThat(hasClinitMethod(originalBytecode)).isTrue();

        // Apply suppression
        byte[] suppressedBytecode = invokeSuppressClinit(originalBytecode);

        // Verify the suppressed bytecode still has a <clinit> (but it's empty)
        assertThat(hasClinitMethod(suppressedBytecode)).isTrue();

        // Verify the suppressed <clinit> is effectively empty (just RETURN)
        assertThat(getClinitInstructionCount(suppressedBytecode)).isEqualTo(1);
    }

    @Test
    public void suppress_clinit_on_class_without_clinit_adds_empty_one() throws Exception {
        // Generate a class without a static initializer
        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name("org.mockito.test.ClassWithoutClinit")
                        .make()
                        .getBytes();

        // Apply suppression
        byte[] suppressedBytecode = invokeSuppressClinit(originalBytecode);

        // Should have an empty <clinit> added
        assertThat(hasClinitMethod(suppressedBytecode)).isTrue();
        assertThat(getClinitInstructionCount(suppressedBytecode)).isEqualTo(1);
    }

    @Test
    public void suppress_clinit_preserves_other_methods() throws Exception {
        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name("org.mockito.test.ClassWithMethods")
                        .defineMethod("myMethod", String.class, Visibility.PUBLIC)
                        .intercept(FixedValue.value("hello"))
                        .make()
                        .getBytes();

        byte[] suppressedBytecode = invokeSuppressClinit(originalBytecode);

        // Verify the class can still be loaded and the non-clinit method is preserved
        assertThat(hasMethod(suppressedBytecode, "myMethod")).isTrue();
    }

    @Test
    public void suppress_clinit_preserves_static_fields_with_zero_defaults() throws Exception {
        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name("org.mockito.test.ClassWithStaticFields")
                        .defineField(
                                "stringField", String.class, Visibility.PUBLIC, Ownership.STATIC)
                        .defineField("intField", int.class, Visibility.PUBLIC, Ownership.STATIC)
                        .defineField(
                                "boolField", boolean.class, Visibility.PUBLIC, Ownership.STATIC)
                        .make()
                        .getBytes();

        byte[] suppressedBytecode = invokeSuppressClinit(originalBytecode);

        // Verify the static fields are still present
        assertThat(hasField(suppressedBytecode, "stringField")).isTrue();
        assertThat(hasField(suppressedBytecode, "intField")).isTrue();
        assertThat(hasField(suppressedBytecode, "boolField")).isTrue();
    }

    @Test
    public void transform_intercepts_suppressed_class_on_initial_load() throws Exception {
        // Get the InlineBytecodeGenerator that's registered as a ClassFileTransformer
        Object inlineBytecodeGenerator = getInlineBytecodeGenerator();

        // Access the suppressedClassNames set via reflection
        Field field = InlineBytecodeGenerator.class.getDeclaredField("suppressedClassNames");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> suppressedClassNames = (Set<String>) field.get(inlineBytecodeGenerator);

        // Generate bytecode for a class with a clinit
        String className = "org.mockito.test.TransformInterceptTarget";
        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name(className)
                        .defineField("VALUE", String.class, Visibility.PUBLIC, Ownership.STATIC)
                        .make()
                        .getBytes();

        try {
            // Add the class name to the suppression set
            suppressedClassNames.add(className);

            // Call transform with classBeingRedefined=null (initial load)
            Method transformMethod =
                    InlineBytecodeGenerator.class.getMethod(
                            "transform",
                            ClassLoader.class,
                            String.class,
                            Class.class,
                            java.security.ProtectionDomain.class,
                            byte[].class);
            byte[] result =
                    (byte[])
                            transformMethod.invoke(
                                    inlineBytecodeGenerator,
                                    null,
                                    className.replace('.', '/'),
                                    null, // classBeingRedefined = null -> initial load
                                    null,
                                    originalBytecode);

            // Should return modified bytecode (not null)
            assertThat(result).isNotNull();
            // The modified bytecode should have an empty clinit
            assertThat(getClinitInstructionCount(result)).isEqualTo(1);
        } finally {
            suppressedClassNames.remove(className);
        }
    }

    @Test
    public void transform_returns_null_for_non_suppressed_class_on_initial_load() throws Exception {
        Object inlineBytecodeGenerator = getInlineBytecodeGenerator();

        byte[] originalBytecode =
                new ByteBuddy()
                        .subclass(Object.class)
                        .name("org.mockito.test.NotSuppressed")
                        .make()
                        .getBytes();

        Method transformMethod =
                InlineBytecodeGenerator.class.getMethod(
                        "transform",
                        ClassLoader.class,
                        String.class,
                        Class.class,
                        java.security.ProtectionDomain.class,
                        byte[].class);
        byte[] result =
                (byte[])
                        transformMethod.invoke(
                                inlineBytecodeGenerator,
                                null,
                                "org/mockito/test/NotSuppressed",
                                null, // initial load
                                null,
                                originalBytecode);

        // Should return null (no transformation) for non-suppressed class
        assertThat(result).isNull();
    }

    @Test
    public void add_and_remove_suppressed_classes_modifies_set() throws Exception {
        Object inlineBytecodeGenerator = getInlineBytecodeGenerator();

        Field field = InlineBytecodeGenerator.class.getDeclaredField("suppressedClassNames");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> suppressedClassNames = (Set<String>) field.get(inlineBytecodeGenerator);

        String testClass = "org.mockito.test.AddRemoveTarget";
        assertThat(suppressedClassNames).doesNotContain(testClass);

        ((BytecodeGenerator) inlineBytecodeGenerator)
                .addSuppressedClasses(Collections.singletonList(testClass));
        assertThat(suppressedClassNames).contains(testClass);

        ((BytecodeGenerator) inlineBytecodeGenerator)
                .removeSuppressedClasses(Collections.singletonList(testClass));
        assertThat(suppressedClassNames).doesNotContain(testClass);
    }

    /**
     * Gets the InlineBytecodeGenerator instance from the active MockMaker via reflection.
     * Navigates: InlineByteBuddyMockMaker -> InlineDelegateByteBuddyMockMaker ->
     * TypeCachingBytecodeGenerator -> InlineBytecodeGenerator
     */
    private Object getInlineBytecodeGenerator() throws Exception {
        MockMaker mockMaker = Plugins.getMockMaker();

        // InlineByteBuddyMockMaker.inlineDelegateByteBuddyMockMaker
        Field delegateField =
                mockMaker.getClass().getDeclaredField("inlineDelegateByteBuddyMockMaker");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(mockMaker);

        // InlineDelegateByteBuddyMockMaker.bytecodeGenerator (TypeCachingBytecodeGenerator)
        Field bytecodeGenField = delegate.getClass().getDeclaredField("bytecodeGenerator");
        bytecodeGenField.setAccessible(true);
        Object typeCaching = bytecodeGenField.get(delegate);

        // TypeCachingBytecodeGenerator.bytecodeGenerator (InlineBytecodeGenerator)
        Field innerGenField =
                TypeCachingBytecodeGenerator.class.getDeclaredField("bytecodeGenerator");
        innerGenField.setAccessible(true);
        return innerGenField.get(typeCaching);
    }

    /**
     * Invokes the private {@code suppressClinit} method via reflection.
     */
    private byte[] invokeSuppressClinit(byte[] bytecode) throws Exception {
        Method method =
                InlineBytecodeGenerator.class.getDeclaredMethod("suppressClinit", byte[].class);
        method.setAccessible(true);
        return (byte[]) method.invoke(null, bytecode);
    }

    private boolean hasClinitMethod(byte[] bytecode) {
        return hasMethod(bytecode, "<clinit>");
    }

    private boolean hasMethod(byte[] bytecode, String methodName) {
        boolean[] found = {false};
        ClassReader reader = new ClassReader(bytecode);
        reader.accept(
                new ClassVisitor(OpenedClassReader.ASM_API) {
                    @Override
                    public MethodVisitor visitMethod(
                            int access,
                            String name,
                            String descriptor,
                            String signature,
                            String[] exceptions) {
                        if (methodName.equals(name)) {
                            found[0] = true;
                        }
                        return null;
                    }
                },
                0);
        return found[0];
    }

    private boolean hasField(byte[] bytecode, String fieldName) {
        boolean[] found = {false};
        ClassReader reader = new ClassReader(bytecode);
        reader.accept(
                new ClassVisitor(OpenedClassReader.ASM_API) {
                    @Override
                    public net.bytebuddy.jar.asm.FieldVisitor visitField(
                            int access,
                            String name,
                            String descriptor,
                            String signature,
                            Object value) {
                        if (fieldName.equals(name)) {
                            found[0] = true;
                        }
                        return null;
                    }
                },
                0);
        return found[0];
    }

    /**
     * Counts the number of bytecode instructions in the {@code <clinit>} method.
     * An empty clinit should have exactly 1 instruction (RETURN).
     */
    private int getClinitInstructionCount(byte[] bytecode) {
        int[] count = {0};
        ClassReader reader = new ClassReader(bytecode);
        reader.accept(
                new ClassVisitor(OpenedClassReader.ASM_API) {
                    @Override
                    public MethodVisitor visitMethod(
                            int access,
                            String name,
                            String descriptor,
                            String signature,
                            String[] exceptions) {
                        if ("<clinit>".equals(name)) {
                            return new MethodVisitor(OpenedClassReader.ASM_API) {
                                @Override
                                public void visitInsn(int opcode) {
                                    count[0]++;
                                }

                                @Override
                                public void visitFieldInsn(
                                        int opcode, String owner, String name, String descriptor) {
                                    count[0]++;
                                }

                                @Override
                                public void visitMethodInsn(
                                        int opcode,
                                        String owner,
                                        String name,
                                        String descriptor,
                                        boolean isInterface) {
                                    count[0]++;
                                }

                                @Override
                                public void visitLdcInsn(Object value) {
                                    count[0]++;
                                }

                                @Override
                                public void visitIntInsn(int opcode, int operand) {
                                    count[0]++;
                                }

                                @Override
                                public void visitVarInsn(int opcode, int varIndex) {
                                    count[0]++;
                                }

                                @Override
                                public void visitTypeInsn(int opcode, String type) {
                                    count[0]++;
                                }
                            };
                        }
                        return null;
                    }
                },
                0);
        return count[0];
    }
}
