/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.configuration;

import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;

public class ClassCacheVersusClassReloadingTest {
    // TODO refactor to use ClassLoaders

    private SimplePerRealmReloadingClassLoader testMethodClassLoaderRealm =
            new SimplePerRealmReloadingClassLoader(reloadMockito());

    @Test
    public void can_mock_classes_with_the_same_name_in_different_class_loaders() throws Exception {
        doInNewChildRealm(
                testMethodClassLoaderRealm,
                "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
        doInNewChildRealm(
                testMethodClassLoaderRealm,
                "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking");
    }

    public static class DoTheMocking implements Callable<Object> {
        public Object call() throws Exception {
            Class<?> clazz =
                    this.getClass()
                            .getClassLoader()
                            .loadClass("org.mockitousage.configuration.ClassToBeMocked");
            return mock(clazz);
        }
    }

    private static void doInNewChildRealm(
            ClassLoader parentRealm, String callableCalledInClassLoaderRealm) throws Exception {
        new SimplePerRealmReloadingClassLoader(parentRealm, reloadScope())
                .doInRealm(callableCalledInClassLoaderRealm);
    }

    private static SimplePerRealmReloadingClassLoader.ReloadClassPredicate reloadScope() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(String qualifiedName) {
                return "org.mockitousage.configuration.ClassCacheVersusClassReloadingTest$DoTheMocking"
                                .equals(qualifiedName)
                        || "org.mockitousage.configuration.ClassToBeMocked".equals(qualifiedName);
            }
        };
    }

    private static SimplePerRealmReloadingClassLoader.ReloadClassPredicate reloadMockito() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(String qualifiedName) {
                return (!qualifiedName.contains("net.bytebuddy")
                        && qualifiedName.contains("org.mockito")
                        && !qualifiedName.contains(
                                "org.mockito.internal.creation.bytebuddy.inject"));
            }
        };
    }
}
