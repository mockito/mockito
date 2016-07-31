package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.internal.creation.bytebuddy.MockFeatures.withMockFeatures;
import static org.mockitoutil.ClassLoaders.inMemoryClassLoader;
import static org.mockitoutil.SimpleClassGenerator.makeMarkerInterface;

public class CachingMockBytecodeGeneratorTest {

    @Before
    public void ensure_disable_gc_is_activated() throws Exception {
        assumeTrue(explicitGCEnabled());
    }

    @Test
    public void ensure_cache_is_cleared_if_no_reference_to_classloader_and_classes() throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache = inMemoryClassLoader()
                .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                .build();

        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator(true);
        Class<?> the_mock_type = cachingMockBytecodeGenerator.get(withMockFeatures(
                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                Collections.<Class<?>>emptySet(),
                false
        ));

        assertThat(cachingMockBytecodeGenerator.avoidingClassLeakageCache).hasSize(1);

        // when
        classloader_with_life_shorter_than_cache = is_no_more_referenced();
        the_mock_type = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        cachingMockBytecodeGenerator.cleanUpCachesForObsoleteClassLoaders();

        // then
        assertThat(cachingMockBytecodeGenerator.avoidingClassLeakageCache).isEmpty();
    }

    @Test
    public void ensure_cache_returns_same_instance() throws Exception {
        // given
        ClassLoader classloader_with_life_shorter_than_cache = inMemoryClassLoader()
                .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                .build();

        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator(true);
        Class<?> the_mock_type = cachingMockBytecodeGenerator.get(withMockFeatures(
                        classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                        Collections.<Class<?>>emptySet(),
                        false
                ));

        Class<?> other_mock_type = cachingMockBytecodeGenerator.get(withMockFeatures(
                classloader_with_life_shorter_than_cache.loadClass("foo.Bar"),
                Collections.<Class<?>>emptySet(),
                false
        ));

        assertThat(other_mock_type).isSameAs(the_mock_type);
        assertThat(cachingMockBytecodeGenerator.avoidingClassLeakageCache).hasSize(1);

        // when
        classloader_with_life_shorter_than_cache = is_no_more_referenced();
        the_mock_type = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        cachingMockBytecodeGenerator.cleanUpCachesForObsoleteClassLoaders();

        // then
        assertThat(cachingMockBytecodeGenerator.avoidingClassLeakageCache).hasSize(1);
    }

    @Test
    public void validate_simple_code_idea_where_weakhashmap_with_classloader_as_key_get_GCed_when_no_more_references() throws Exception {
        // given
        WeakHashMap<ClassLoader, Object> cache = new WeakHashMap<ClassLoader, Object>();
        ClassLoader short_lived_classloader = inMemoryClassLoader()
                .withClassDefinition("foo.Bar", makeMarkerInterface("foo.Bar"))
                .build();

        cache.put(short_lived_classloader, new HoldingAReference(new WeakReference<Class<?>>(short_lived_classloader.loadClass("foo.Bar"))));

        assertThat(cache).hasSize(1);

        // when
        short_lived_classloader = is_no_more_referenced();

        System.gc();
        ensure_gc_happened();

        // then
        assertThat(cache).isEmpty();
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

    private static boolean explicitGCEnabled() {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String inputArgument : inputArguments) {
            if (inputArgument.contains("-XX:+DisableExplicitGC")) {
                return false;
            }
        }
        return true;
    }

    private static void ensure_gc_happened() throws InterruptedException {
        // wait in order to make sure the GC happened
        Thread.sleep(500);
    }
}
