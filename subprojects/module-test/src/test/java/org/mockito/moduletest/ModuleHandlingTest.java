/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import net.bytebuddy.dynamic.loading.ClassInjector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;
import static org.mockito.moduletest.ModuleUtil.layer;
import static org.mockito.moduletest.ModuleUtil.modularJar;

@RunWith(Parameterized.class)
public class ModuleHandlingTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {true}, {false}
        });
    }

    private final boolean namedModules;

    public ModuleHandlingTest(boolean namedModules) {
        this.namedModules = namedModules;
    }

    @Test
    public void can_define_class_in_open_reading_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(true, true, true);
        ModuleLayer layer = layer(jar, true, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void can_define_class_in_open_java_util_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(true, true, true);
        ModuleLayer layer = layer(jar, true, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("java.util.concurrent.locks.Lock");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Lock mock = (Lock) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.tryLock());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenReturn", Object.class).invoke(stubbing, true);

            boolean relocated = !Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable();
            String prefix = relocated ? "org.mockito.codegen.Lock$MockitoMock$" : "java.util.concurrent.locks.Lock$MockitoMock$";
            assertThat(mock.getClass().getName()).startsWith(prefix);
            assertThat(mock.tryLock()).isEqualTo(true);
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void inline_mock_maker_can_mock_closed_modules() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(true));

        Path jar = modularJar(false, false, false);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        Class<?> mockito = loader.loadClass(Mockito.class.getName());
        @SuppressWarnings("unchecked")
        Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
        Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
        loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

        assertThat(mock.getClass().getName()).isEqualTo("sample.MyCallable");
        assertThat(mock.call()).isEqualTo("foo");
    }

    @Test
    public void can_define_class_in_open_reading_private_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(false, true, true);
        ModuleLayer layer = layer(jar, true, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void can_define_class_in_open_non_reading_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(true, true, true);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void can_define_class_in_open_non_reading_non_exporting_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(true, false, true);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void can_define_class_in_closed_module() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));

        Path jar = modularJar(true, true, false);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            boolean relocated = !Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable();
            String prefix = relocated ? "sample.MyCallable$MockitoMock$" : "org.mockito.codegen.MyCallable$MockitoMock$";
            assertThat(mock.getClass().getName()).startsWith(prefix);
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void cannot_define_class_in_non_opened_non_exported_module_if_lookup_injection() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));
        assumeThat(!Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable(), is(true));

        Path jar = modularJar(false, false, false);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass(OngoingStubbing.class.getName()).getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void can_define_class_in_non_opened_non_exported_module_if_unsafe_injection() throws Exception {
        assumeThat(Plugins.getMockMaker() instanceof InlineByteBuddyMockMaker, is(false));
        assumeThat(!Boolean.getBoolean("org.mockito.internal.noUnsafeInjection") && ClassInjector.UsingReflection.isAvailable(), is(false));

        Path jar = modularJar(false, false, false);
        ModuleLayer layer = layer(jar, false, namedModules);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass(Mockito.class.getName());
            try {
                mockito.getMethod("mock", Class.class).invoke(null, type);
                fail("Expected mocking to fail");
            } catch (InvocationTargetException e) {
                assertThat(e.getTargetException()).isInstanceOf(loader.loadClass(MockitoException.class.getName()));
            }
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }
}
