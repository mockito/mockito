/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.utility.OpenedClassReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ClinitSuppressionTransformerTest {

    // Test target class with a static initializer
    static class WithClinit implements Serializable {
        static String value = "initialized";
        static int number = 42;

        public String getValue() {
            return value;
        }
    }

    // Test target class without a static initializer
    static class WithoutClinit {
        public String hello() {
            return "hello";
        }
    }

    // Test target class with methods and fields
    static class WithClinitAndMethods {
        static String staticField = "static";
        String instanceField = "instance";

        static String staticMethod() {
            return "staticMethod";
        }

        String instanceMethod() {
            return "instanceMethod";
        }
    }

    @Test
    public void suppress_clinit_removes_static_initializer() throws IOException {
        byte[] original = readClassBytes(WithClinit.class);
        assertThat(hasClinit(original)).isTrue();

        byte[] transformed = ClinitSuppressionTransformer.suppressClinit(original);
        assertThat(hasClinit(transformed)).isTrue();

        // The clinit should now be empty (just RETURN), verify by checking the bytecode is
        // different
        assertThat(transformed).isNotEqualTo(original);
    }

    @Test
    public void suppress_clinit_on_class_without_clinit_is_harmless() throws IOException {
        byte[] original = readClassBytes(WithoutClinit.class);
        assertThat(hasClinit(original)).isFalse();

        byte[] result = ClinitSuppressionTransformer.suppressClinit(original);
        // Should return null when no clinit is found
        assertThat(result).isNull();
    }

    @Test
    public void suppress_clinit_preserves_other_methods_and_fields() throws IOException {
        byte[] original = readClassBytes(WithClinitAndMethods.class);
        byte[] transformed = ClinitSuppressionTransformer.suppressClinit(original);

        Set<String> originalMethods = collectMethodNames(original);
        Set<String> transformedMethods = collectMethodNames(transformed);

        assertThat(transformedMethods).containsAll(originalMethods);
    }

    @Test
    public void suppress_clinit_leaves_static_fields_at_zero_defaults() throws Exception {
        byte[] original = readClassBytes(WithClinit.class);
        byte[] transformed = ClinitSuppressionTransformer.suppressClinit(original);

        // Load the transformed class with a custom classloader
        String className = WithClinit.class.getName();
        ClassLoader customLoader =
                new ClassOverridingLoader(getClass().getClassLoader(), className, transformed);

        Class<?> loaded = customLoader.loadClass(className);
        Field valueField = loaded.getDeclaredField("value");
        valueField.setAccessible(true);
        assertThat(valueField.get(null)).isNull();

        Field numberField = loaded.getDeclaredField("number");
        numberField.setAccessible(true);
        assertThat(numberField.get(null)).isEqualTo(0);
    }

    @Test
    public void suppress_clinit_preserves_implicit_serialVersionUID() throws Exception {
        String className = WithClinit.class.getName();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(new WithClinit());
        }
        byte[] serializedBytes = outputStream.toByteArray();
        byte[] transformed =
                ClinitSuppressionTransformer.suppressClinit(readClassBytes(WithClinit.class));

        assertThat(transformed).isNotNull();
        ClassLoader customLoader =
                new ClassOverridingLoader(getClass().getClassLoader(), className, transformed);

        try (ObjectInputStream inputStream =
                getInputStream(serializedBytes, className, customLoader)) {
            Object deserialized = inputStream.readObject();
            assertThat(deserialized.getClass().getName()).isEqualTo(className);
        }
    }

    @Test
    public void transformer_returns_null_for_non_matching_class() throws IOException {
        Predicate<String> matcher = "com.example.OtherClass"::equals;
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(matcher);

        byte[] result =
                transformer.transform(
                        getClass().getClassLoader(),
                        WithClinit.class.getName().replace('.', '/'),
                        null,
                        null,
                        readClassBytes(WithClinit.class));

        assertThat(result).isNull();
    }

    @Test
    public void transformer_returns_null_for_retransformation() throws IOException {
        Predicate<String> matcher = WithClinit.class.getName()::equals;
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(matcher);

        byte[] result =
                transformer.transform(
                        getClass().getClassLoader(),
                        WithClinit.class.getName().replace('.', '/'),
                        WithClinit.class, // non-null means retransformation
                        null,
                        readClassBytes(WithClinit.class));

        assertThat(result).isNull();
    }

    @Test
    public void transformer_suppresses_matching_class_on_initial_load() throws IOException {
        Predicate<String> matcher = WithClinit.class.getName()::equals;
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(matcher);

        byte[] original = readClassBytes(WithClinit.class);
        byte[] result =
                transformer.transform(
                        getClass().getClassLoader(),
                        WithClinit.class.getName().replace('.', '/'),
                        null,
                        null,
                        original);

        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(original);
    }

    @Test
    public void transformer_suppresses_class_matched_by_package_predicate() throws IOException {
        String packagePrefix = WithClinit.class.getPackage().getName() + ".";
        Predicate<String> matcher = name -> name.startsWith(packagePrefix);
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(matcher);

        byte[] original = readClassBytes(WithClinit.class);
        byte[] result =
                transformer.transform(
                        getClass().getClassLoader(),
                        WithClinit.class.getName().replace('.', '/'),
                        null,
                        null,
                        original);

        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(original);
    }

    private static byte[] readClassBytes(Class<?> clazz) throws IOException {
        String resourceName = clazz.getName().replace('.', '/') + ".class";
        try (InputStream is = clazz.getClassLoader().getResourceAsStream(resourceName)) {
            assertThat(is).isNotNull();
            return is.readAllBytes();
        }
    }

    private static boolean hasClinit(byte[] classBytes) {
        boolean[] found = {false};
        new ClassReader(classBytes)
                .accept(
                        new ClassVisitor(OpenedClassReader.ASM_API) {
                            @Override
                            public MethodVisitor visitMethod(
                                    int access,
                                    String name,
                                    String descriptor,
                                    String signature,
                                    String[] exceptions) {
                                if ("<clinit>".equals(name)) {
                                    found[0] = true;
                                }
                                return null;
                            }
                        },
                        0);
        return found[0];
    }

    private static Set<String> collectMethodNames(byte[] classBytes) {
        Set<String> names = new HashSet<>();
        new ClassReader(classBytes)
                .accept(
                        new ClassVisitor(OpenedClassReader.ASM_API) {
                            @Override
                            public MethodVisitor visitMethod(
                                    int access,
                                    String name,
                                    String descriptor,
                                    String signature,
                                    String[] exceptions) {
                                names.add(name);
                                return null;
                            }
                        },
                        0);
        return names;
    }

    private static ObjectInputStream getInputStream(
            byte[] serializedBytes, String className, ClassLoader customLoader) throws IOException {
        return new ObjectInputStream(new ByteArrayInputStream(serializedBytes)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass clazz)
                    throws IOException, ClassNotFoundException {
                if (className.equals(clazz.getName())) {
                    return customLoader.loadClass(className);
                }
                return super.resolveClass(clazz);
            }
        };
    }

    private static class ClassOverridingLoader extends ClassLoader {
        final String target;
        final byte[] bytecode;

        ClassOverridingLoader(ClassLoader parent, String target, byte[] bytecode) {
            super(parent);
            this.target = target;
            this.bytecode = bytecode;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (name.equals(target)) {
                return defineClass(name, bytecode, 0, bytecode.length);
            }
            return super.loadClass(name, resolve);
        }
    }
}
