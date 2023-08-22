/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.creation.bytebuddy.MockFeatures.withMockFeatures;
import static org.mockitoutil.ClassLoaders.inMemoryClassLoader;
import static org.mockitoutil.SimpleClassGenerator.makeMarkerInterface;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.mock.SerializableMode;
import org.mockitoutil.VmArgAssumptions;

public class TypeCachingMockBytecodeGeneratorTest {

    @Before
    public void ensure_disable_gc_is_activated() throws Exception {
        VmArgAssumptions.assumeVmArgNotPresent("-XX:+DisableExplicitGC");
    }

    @Test
    public void ensure_cache_is_cleared_if_no_reference_to_classloader_and_classes()
            throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache =
                inMemoryClassLoader()
                        .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                        .build();

        TypeCachingBytecodeGenerator cachingMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), true);

        Class<?> the_mock_type =
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.NONE,
                                false,
                                Answers.RETURNS_DEFAULTS));

        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
        Reference<Object> typeReference =
                new PhantomReference<Object>(the_mock_type, referenceQueue);

        // when
        classloader_with_life_shorter_than_cache = is_no_more_referenced();
        the_mock_type = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        // then
        assertThat(referenceQueue.poll()).isEqualTo(typeReference);
    }

    @Test
    public void ensure_cache_returns_same_instance() throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache =
                inMemoryClassLoader()
                        .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                        .build();

        TypeCachingBytecodeGenerator cachingMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), true);
        Class<?> the_mock_type =
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.NONE,
                                false,
                                Answers.RETURNS_DEFAULTS));

        Class<?> other_mock_type =
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.NONE,
                                false,
                                Answers.RETURNS_DEFAULTS));

        assertThat(other_mock_type).isSameAs(the_mock_type);

        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
        Reference<Object> typeReference =
                new PhantomReference<Object>(the_mock_type, referenceQueue);

        // when
        classloader_with_life_shorter_than_cache = is_no_more_referenced();
        the_mock_type = is_no_more_referenced();
        other_mock_type = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        // then
        assertThat(referenceQueue.poll()).isEqualTo(typeReference);
    }

    @Test
    public void ensure_cache_returns_different_instance_serializableMode() throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache =
                inMemoryClassLoader()
                        .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                        .build();

        TypeCachingBytecodeGenerator cachingMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), true);
        Class<?> the_mock_type =
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.NONE,
                                false,
                                Answers.RETURNS_DEFAULTS));

        Class<?> other_mock_type =
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.BASIC,
                                false,
                                Answers.RETURNS_DEFAULTS));

        assertThat(other_mock_type).isNotSameAs(the_mock_type);
    }

    @Test
    public void ensure_cache_returns_same_instance_defaultAnswer() throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache =
                inMemoryClassLoader()
                        .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                        .build();

        TypeCachingBytecodeGenerator cachingMockBytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), true);

        Answers[] answers = Answers.values();
        Set<Class<?>> classes = Collections.newSetFromMap(new IdentityHashMap<>());
        classes.add(
                cachingMockBytecodeGenerator.mockClass(
                        withMockFeatures(
                                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                Collections.<Class<?>>emptySet(),
                                SerializableMode.NONE,
                                false,
                                null)));
        for (Answers answer : answers) {
            Class<?> klass =
                    cachingMockBytecodeGenerator.mockClass(
                            withMockFeatures(
                                    classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                                    Collections.<Class<?>>emptySet(),
                                    SerializableMode.NONE,
                                    false,
                                    answer));
            assertThat(classes.add(klass)).isFalse();
        }

        assertThat(classes).hasSize(1);
    }

    @Test
    public void
            validate_simple_code_idea_where_weakhashmap_with_classloader_as_key_get_GCed_when_no_more_references()
                    throws Exception {
        // given
        WeakHashMap<ClassLoader, Object> cache = new WeakHashMap<ClassLoader, Object>();
        ClassLoader short_lived_classloader =
                inMemoryClassLoader()
                        .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                        .build();

        cache.put(
                short_lived_classloader,
                new HoldingAReference(
                        new WeakReference<Class<?>>(short_lived_classloader.loadClass("foo.Bar"))));

        assertThat(cache).hasSize(1);

        // when
        short_lived_classloader = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        // then
        assertThat(cache).isEmpty();
    }

    @Test
    public void cacheLockingStressTest_same_hashcode_different_interface()
            throws InterruptedException, TimeoutException {
        Class<?>[] classes = cacheLockingInMemClassLoaderClasses();
        Class<?> ifA = classes[0];
        Class<?> ifB = classes[1];
        var featA = newMockFeatures(ifA, ifB);
        var featB = newMockFeatures(ifB, ifA);
        cacheLockingStressTestImpl(featA, featB);
    }

    @Test
    public void cacheLockingStressTest_same_hashcode_same_interface()
            throws InterruptedException, TimeoutException {
        Class<?>[] classes = cacheLockingInMemClassLoaderClasses();
        Class<?> ifA = classes[0];
        var featA = newMockFeatures(ifA);
        cacheLockingStressTestImpl(featA, featA);
    }

    @Test
    public void cacheLockingStressTest_different_hashcode()
            throws InterruptedException, TimeoutException {
        Class<?>[] classes = cacheLockingInMemClassLoaderClasses();
        Class<?> ifA = classes[0];
        Class<?> ifB = classes[1];
        Class<?> ifC = classes[2];
        var featA = newMockFeatures(ifA, ifB);
        var featB = newMockFeatures(ifB, ifC);
        cacheLockingStressTestImpl(featA, featB);
    }

    @Test
    public void cacheLockingStressTest_unrelated_classes()
            throws InterruptedException, TimeoutException {
        Class<?>[] classes = cacheLockingInMemClassLoaderClasses();
        Class<?> ifA = classes[0];
        Class<?> ifB = classes[1];
        var featA = newMockFeatures(ifA);
        var featB = newMockFeatures(ifB);
        cacheLockingStressTestImpl(featA, featB);
    }

    private void cacheLockingStressTestImpl(MockFeatures<?> featA, MockFeatures<?> featB)
            throws InterruptedException, TimeoutException {
        int iterations = 10_000;

        TypeCachingBytecodeGenerator bytecodeGenerator =
                new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(), true);

        Phaser phaser = new Phaser(4);
        Function<Runnable, CompletableFuture<Void>> runCode =
                code ->
                        CompletableFuture.runAsync(
                                () -> {
                                    phaser.arriveAndAwaitAdvance();
                                    try {
                                        for (int i = 0; i < iterations; i++) {
                                            code.run();
                                        }
                                    } finally {
                                        phaser.arrive();
                                    }
                                });
        var mockFeatAFuture =
                runCode.apply(
                        () -> {
                            Class<?> mockClass = bytecodeGenerator.mockClass(featA);
                            assertValidMockClass(featA, mockClass);
                        });

        var mockFeatBFuture =
                runCode.apply(
                        () -> {
                            Class<?> mockClass = bytecodeGenerator.mockClass(featB);
                            assertValidMockClass(featB, mockClass);
                        });
        var cacheFuture = runCode.apply(bytecodeGenerator::clearAllCaches);
        // Start test
        phaser.arriveAndAwaitAdvance();
        // Wait for test to end
        int phase = phaser.arrive();
        try {

            phaser.awaitAdvanceInterruptibly(phase, 30, TimeUnit.SECONDS);
        } finally {
            // Collect exceptions from the futures, to make issues visible.
            mockFeatAFuture.getNow(null);
            mockFeatBFuture.getNow(null);
            cacheFuture.getNow(null);
        }
    }

    private static <T> MockFeatures<T> newMockFeatures(
            Class<T> mockedType, Class<?>... interfaces) {
        return MockFeatures.withMockFeatures(
                mockedType,
                new HashSet<>(Arrays.asList(interfaces)),
                SerializableMode.NONE,
                false,
                null);
    }

    private static Class<?>[] cacheLockingInMemClassLoaderClasses() {
        ClassLoader inMemClassLoader =
                inMemoryClassLoader()
                        .withClassDefinition("foo.IfA", makeMarkerInterface("foo.IfA"))
                        .withClassDefinition("foo.IfB", makeMarkerInterface("foo.IfB"))
                        .withClassDefinition("foo.IfC", makeMarkerInterface("foo.IfC"))
                        .build();
        try {
            return new Class[] {
                inMemClassLoader.loadClass("foo.IfA"),
                inMemClassLoader.loadClass("foo.IfB"),
                inMemClassLoader.loadClass("foo.IfC")
            };
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void assertValidMockClass(MockFeatures<?> mockFeature, Class<?> mockClass) {
        assertThat(mockClass).isAssignableTo(mockFeature.mockedType);
        for (Class<?> anInterface : mockFeature.interfaces) {
            assertThat(mockClass).isAssignableTo(anInterface);
        }
    }

    static class HoldingAReference {
        final WeakReference<Class<?>> a;

        HoldingAReference(WeakReference<Class<?>> a) {
            this.a = a;
        }
    }

    private static <T> T is_no_more_referenced() {
        return null;
    }

    private static void ensure_gc_happened() throws InterruptedException {
        // wait in order to make sure the GC happened
        Thread.sleep(500);
    }
}
