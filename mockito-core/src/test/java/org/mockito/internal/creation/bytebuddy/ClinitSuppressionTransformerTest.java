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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ClinitSuppressionTransformerTest {

    // Test target class with a static initializer
    static class WithClinit {
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
                new ClassLoader(getClass().getClassLoader()) {
                    @Override
                    protected Class<?> loadClass(String name, boolean resolve)
                            throws ClassNotFoundException {
                        if (name.equals(className)) {
                            return defineClass(name, transformed, 0, transformed.length);
                        }
                        return super.loadClass(name, resolve);
                    }
                };

        Class<?> loaded = customLoader.loadClass(className);
        Field valueField = loaded.getDeclaredField("value");
        valueField.setAccessible(true);
        assertThat(valueField.get(null)).isNull();

        Field numberField = loaded.getDeclaredField("number");
        numberField.setAccessible(true);
        assertThat(numberField.get(null)).isEqualTo(0);
    }

    @Test
    public void transformer_returns_null_for_non_matching_class() throws IOException {
        Set<String> names = Collections.singleton("com.example.OtherClass");
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(names);

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
        Set<String> names = Collections.singleton(WithClinit.class.getName());
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(names);

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
        Set<String> names = Collections.singleton(WithClinit.class.getName());
        ClinitSuppressionTransformer transformer = new ClinitSuppressionTransformer(names);

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
}
