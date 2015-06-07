/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.configuration;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;

import org.fest.assertions.Condition;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ClassCacheVersusClassReloadingTest {
    // TODO refactor to use ClassLoaders

    private final SimplePerRealmReloadingClassLoader testMethodClassLoaderRealm = new SimplePerRealmReloadingClassLoader(reloadMockito());

    @Test
    public void should_throw_ClassCastException_on_second_call() throws Exception {
        Assume.assumeTrue("CglibMockMaker".equals(Plugins.getMockMaker().getClass().getSimpleName()));

        doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");

        try {
            doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
            fail("should have raised a ClassCastException when Objenesis Cache is enabled");
        } catch (final MockitoException e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("classloading")
                    .containsIgnoringCase("objenesis")
                    .containsIgnoringCase("MockitoConfiguration");
            assertThat(e.getCause())
                    .satisfies(thatCceIsThrownFrom("java.lang.Class.cast"))
                    .satisfies(thatCceIsThrownFrom("org.mockito.internal.creation.cglib.ClassImposterizer.imposterise"));
        }
    }

    @Test
    public void should_not_throw_ClassCastException_when_objenesis_cache_disabled() throws Exception {
        prepareMockitoAndDisableObjenesisCache();

        doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
        doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
    }

    private Condition<Throwable> thatCceIsThrownFrom(final String stacktraceElementDescription) {
        return new Condition<Throwable>() {
            @Override
            public boolean matches(final Throwable throwable) {
                final StackTraceElement[] stackTrace = throwable.getStackTrace();
                for (final StackTraceElement stackTraceElement : stackTrace) {
                    if (stackTraceElement.toString().contains(stacktraceElementDescription)) {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    public static class DoTheMocking implements Callable {
        public Object call() throws Exception {
            final Class clazz = this.getClass().getClassLoader().loadClass("org.mockitousage.configuration.ClassToBeMocked");
            return mock(clazz);
        }
    }


    private static void doInNewChildRealm(final ClassLoader parentRealm, final String callableCalledInClassLoaderRealm) throws Exception {
        final SimplePerRealmReloadingClassLoader simplePerRealmReloadingClassLoader = new SimplePerRealmReloadingClassLoader(parentRealm, reloadScope());
        simplePerRealmReloadingClassLoader.doInRealm(callableCalledInClassLoaderRealm);
        simplePerRealmReloadingClassLoader.close();
    }

    private static SimplePerRealmReloadingClassLoader.ReloadClassPredicate reloadScope() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(final String qualifiedName) {
                return "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking".equals(qualifiedName)
                    || "org.mockitousage.configuration.ClassToBeMocked".equals(qualifiedName);
            }
        };
    }

    private void prepareMockitoAndDisableObjenesisCache() throws Exception {
        testMethodClassLoaderRealm.doInRealm("org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$PrepareMockito");
    }

    public static class PrepareMockito implements Callable {
        public Object call() throws Exception {
            Class.forName("org.mockito.Mockito");
            ConfigurationAccess.getConfig().overrideEnableClassCache(false);
            return Boolean.TRUE;
        }
    }

    private static SimplePerRealmReloadingClassLoader.ReloadClassPredicate reloadMockito() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(final String qualifiedName) {
                return (!qualifiedName.contains("net.bytebuddy") && qualifiedName.contains("org.mockito"));
            }
        };
    }

}
