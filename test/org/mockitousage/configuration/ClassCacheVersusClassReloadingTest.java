package org.mockitousage.configuration;


import org.fest.assertions.Condition;
import org.junit.Test;
import org.mockito.internal.configuration.ConfigurationAccess;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class ClassCacheVersusClassReloadingTest {

    private SimplePerRealmReloadingClassLoader testMethodClassLoaderRealm = new SimplePerRealmReloadingClassLoader(reloadMockito());

    @Test
    public void should_throw_ClassCastException_on_second_call() throws Exception {
        doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");

        try {
            doInNewChildRealm(testMethodClassLoaderRealm, "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
            fail("should have raised a ClasCastException when Objenis Cache is enabled");
        } catch (ClassCastException e) {
            assertThat(e).satisfies(thatCceIsThrownFrom("org.mockito.internal.creation.jmock.ClassImposterizer.imposterise"));
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
            public boolean matches(Throwable throwable) {
                return throwable.getStackTrace()[1].toString().contains(stacktraceElementDescription);
            }
        };
    }

    public static class DoTheMocking implements Callable {
        public Object call() throws Exception {
            Class clazz = this.getClass().getClassLoader().loadClass("org.mockitousage.MethodsImpl");
            return mock(clazz);
        }
    }


    private static void doInNewChildRealm(ClassLoader parentRealm, String callableCalledInClassLoaderRealm) throws Exception {
        new SimplePerRealmReloadingClassLoader(parentRealm, reloadScope()).doInRealm(callableCalledInClassLoaderRealm);
    }

    private static SimplePerRealmReloadingClassLoader.ReloadClassPredicate reloadScope() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean needReload(String qualifiedName) {
                return "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking".equals(qualifiedName)
                    || "org.mockitousage.MethodsImpl".equals(qualifiedName);
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
            public boolean needReload(String qualifiedName) {
                return qualifiedName.contains("org.mockito")
                        && !qualifiedName.contains("org.mockito.cglib");
            }
        };
    }

    /**
     * Custom classloader to load classes in hierarchic realm.
     *
     * Each class can be reloaded in the realm if the LoadClassPredicate says so.
     */
    private static class SimplePerRealmReloadingClassLoader extends URLClassLoader {

        private ReloadClassPredicate reloadClassPredicate;

        public SimplePerRealmReloadingClassLoader(ReloadClassPredicate reloadClassPredicate) {
            super(new URL[]{obtainClassPath(SimplePerRealmReloadingClassLoader.class)});
            this.reloadClassPredicate = reloadClassPredicate;
        }

        public SimplePerRealmReloadingClassLoader(ClassLoader parentClassLoader, ReloadClassPredicate reloadClassPredicate) {
            super(new URL[]{obtainClassPath(SimplePerRealmReloadingClassLoader.class)}, parentClassLoader);
            this.reloadClassPredicate = reloadClassPredicate;
        }

        private static URL obtainClassPath(Class<SimplePerRealmReloadingClassLoader> aClass) {
            String path = aClass.getName().replace('.', '/') + ".class";
            String url = aClass.getClassLoader().getResource(path).toExternalForm();

            try {
                return new URL(url.substring(0, url.length() - path.length()));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Classloader couldn't obtain a proper classpath URL", e);
            }
        }

        @Override
        public Class<?> loadClass(String qualifiedName) throws ClassNotFoundException {
            if(reloadClassPredicate.needReload(qualifiedName)) {
                // return customLoadClass(qualifiedName);
                return findClass(qualifiedName);
            }
            return super.loadClass(qualifiedName);
        }

        public Object doInRealm(String callableCalledInClassLoaderRealm) throws Exception {
            Callable<?> callableInRealm = (Callable<?>) this.loadClass(callableCalledInClassLoaderRealm).newInstance();
            return callableInRealm.call();
        }

        public static interface ReloadClassPredicate {
            boolean needReload(String qualifiedName);
        }
    }
}
