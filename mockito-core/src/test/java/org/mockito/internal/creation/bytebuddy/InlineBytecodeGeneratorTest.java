/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.bytebuddy.dynamic.ClassFileLocator;
import org.junit.Test;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;

public class InlineBytecodeGeneratorTest {

    @Test
    public void clearing_does_not_transform_classes_on_another_thread() throws Exception {
        new InlineByteBuddyMockMaker();
        Instrumentation instrumentation = mock(Instrumentation.class);
        InlineBytecodeGenerator generator =
                new InlineBytecodeGenerator(
                        instrumentation,
                        new WeakConcurrentMap<>(false),
                        new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL),
                        new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.MANUAL),
                        type -> false,
                        (type, object, arguments, parameterTypeNames) -> object);
        byte[] original = ClassFileLocator.ForClassLoader.read(Sample.class);
        Callable<byte[]> transform =
                () ->
                        generator.transform(
                                Sample.class.getClassLoader(),
                                Sample.class.getName(),
                                Sample.class,
                                null,
                                original);
        generator.mockClassConstruction(Sample.class);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            doAnswer(
                            invocation -> {
                                assertThat(executor.submit(transform).get(10, TimeUnit.SECONDS))
                                        .isNull();
                                return null;
                            })
                    .when(instrumentation)
                    .retransformClasses(any(Class[].class));

            generator.clearAllCaches();

            assertThat(transform.call()).isNull();
        } finally {
            executor.shutdownNow();
        }
    }

    static class Sample {}
}
