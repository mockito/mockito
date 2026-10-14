/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.bytebuddy.dynamic.ClassFileLocator;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;

public class InlineBytecodeGeneratorTest {

    @Test
    public void clearing_does_not_transform_classes_on_another_thread() throws Exception {
        Instrumentation instrumentation = mock(Instrumentation.class);
        InlineBytecodeGenerator generator = newGenerator(instrumentation);
        byte[] original = ClassFileLocator.ForClassLoader.read(Sample.class);
        generator.mockClassConstruction(Sample.class);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            doAnswer(
                            invocation -> {
                                assertThat(
                                                executor.submit(
                                                                () ->
                                                                        transform(
                                                                                generator,
                                                                                original))
                                                        .get(10, TimeUnit.SECONDS))
                                        .isNull();
                                return null;
                            })
                    .when(instrumentation)
                    .retransformClasses(any(Class[].class));

            generator.clearAllCaches();

            assertThat(transform(generator, original)).isNull();
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    public void transformation_failures_are_scoped_to_their_operation() throws Exception {
        Instrumentation instrumentation = mock(Instrumentation.class);
        InlineBytecodeGenerator generator = newGenerator(instrumentation);
        byte[] original = ClassFileLocator.ForClassLoader.read(Sample.class);
        generator.mockClassConstruction(Sample.class);
        assertThat(transform(generator, new byte[0])).isNull();
        doAnswer(
                        invocation -> {
                            assertThat(transform(generator, original)).isNotNull();
                            return null;
                        })
                .when(instrumentation)
                .retransformClasses(any(Class[].class));

        generator.clearAllCaches();
        generator.mockClassConstruction(Sample.class);
        generator.clearAllCaches();

        doAnswer(
                        invocation -> {
                            assertThat(transform(generator, new byte[0])).isNull();
                            return null;
                        })
                .when(instrumentation)
                .retransformClasses(any(Class[].class));

        assertThatThrownBy(() -> generator.mockClassConstruction(Sample.class))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Could not modify all classes");
    }

    @Test
    public void clearing_reports_failure_from_its_transformation() throws Exception {
        Instrumentation instrumentation = mock(Instrumentation.class);
        InlineBytecodeGenerator generator = newGenerator(instrumentation);
        byte[] original = ClassFileLocator.ForClassLoader.read(Sample.class);
        generator.mockClassConstruction(Sample.class);
        doAnswer(
                        invocation -> {
                            assertThat(transform(generator, new byte[0])).isNull();
                            return null;
                        })
                .when(instrumentation)
                .retransformClasses(any(Class[].class));

        assertThatThrownBy(generator::clearAllCaches)
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Could not reset all classes");
        assertThat(transform(generator, original)).isNull();
    }

    private static InlineBytecodeGenerator newGenerator(Instrumentation instrumentation) {
        new InlineByteBuddyMockMaker();
        return new InlineBytecodeGenerator(
                instrumentation,
                new WeakConcurrentMap<>(false),
                new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL),
                new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL),
                type -> false,
                (type, object, arguments, parameterTypeNames) -> object);
    }

    private static byte[] transform(InlineBytecodeGenerator generator, byte[] bytes) {
        return generator.transform(
                Sample.class.getClassLoader(), Sample.class.getName(), Sample.class, null, bytes);
    }

    static class Sample {}
}
