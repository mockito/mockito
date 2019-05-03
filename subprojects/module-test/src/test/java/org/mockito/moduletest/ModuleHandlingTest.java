/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import java.util.concurrent.locks.Lock;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.ModuleVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.OpenedClassReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;

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
        ModuleLayer layer = layer(jar, true);

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
        ModuleLayer layer = layer(jar, true);

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
        ModuleLayer layer = layer(jar, false);

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
        ModuleLayer layer = layer(jar, true);

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
        ModuleLayer layer = layer(jar, false);

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
        ModuleLayer layer = layer(jar, false);

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
        ModuleLayer layer = layer(jar, false);

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
        ModuleLayer layer = layer(jar, false);

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
        ModuleLayer layer = layer(jar, false);

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

    private static Path modularJar(boolean isPublic, boolean isExported, boolean isOpened) throws IOException {
        Path jar = Files.createTempFile("sample-module", ".jar");
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jar))) {
            out.putNextEntry(new JarEntry("module-info.class"));
            out.write(moduleInfo(isExported, isOpened));
            out.closeEntry();
            out.putNextEntry(new JarEntry("sample/MyCallable.class"));
            out.write(type(isPublic));
            out.closeEntry();
        }
        return jar;
    }

    private static byte[] type(boolean isPublic) {
        return new ByteBuddy()
            .subclass(Callable.class)
            .name("sample.MyCallable")
            .merge(isPublic ? Visibility.PUBLIC : Visibility.PACKAGE_PRIVATE)
            .method(named("call"))
            .intercept(FixedValue.value("foo"))
            .make()
            .getBytes();
    }

    private static byte[] moduleInfo(boolean isExported, boolean isOpened) {
        ClassWriter classWriter = new ClassWriter(OpenedClassReader.ASM_API);
        classWriter.visit(Opcodes.V9, Opcodes.ACC_MODULE, "module-info", null, null, null);
        ModuleVisitor mv = classWriter.visitModule("mockito.test", 0, null);
        mv.visitRequire("java.base", Opcodes.ACC_MANDATED, null);
        mv.visitPackage("sample");
        if (isExported) {
            mv.visitExport("sample", 0);
        }
        if (isOpened) {
            mv.visitOpen("sample", 0);
        }
        mv.visitEnd();
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private ModuleLayer layer(Path jar, boolean canRead) throws MalformedURLException {
        Set<String> modules = new HashSet<>();
        modules.add("mockito.test");
        ModuleFinder moduleFinder = ModuleFinder.of(jar);
        if (namedModules) {
            modules.add("org.mockito");
            modules.add("net.bytebuddy");
            modules.add("net.bytebuddy.agent");
            // We do not list all packages but only roots and packages that interact with the mock where
            // we attempt to validate an interaction of two modules. This is of course a bit hacky as those
            // libraries would normally be entirely encapsulated in an automatic module with all their classes
            // but it is sufficient for the test and saves us a significant amount of code.
            moduleFinder = ModuleFinder.compose(moduleFinder,
                automaticModule("org.mockito", "org.mockito", "org.mockito.internal.creation.bytebuddy"),
                automaticModule("net.bytebuddy", "net.bytebuddy"),
                automaticModule("net.bytebuddy.agent", "net.bytebuddy.agent"));
        }
        Configuration configuration = Configuration.resolve(
            moduleFinder,
            Collections.singletonList(ModuleLayer.boot().configuration()),
            ModuleFinder.of(),
            modules
        );
        ClassLoader classLoader = new ReplicatingClassLoader(jar);
        ModuleLayer.Controller controller = ModuleLayer.defineModules(
            configuration,
            Collections.singletonList(ModuleLayer.boot()),
            module -> classLoader
        );
        if (canRead) {
            controller.addReads(
                controller.layer().findModule("mockito.test").orElseThrow(IllegalStateException::new),
                Mockito.class.getModule()
            );
        }
        return controller.layer();
    }

    private static ModuleFinder automaticModule(String moduleName, String... packages) {
        ModuleDescriptor descriptor = ModuleDescriptor.newAutomaticModule(moduleName)
            .packages(new HashSet<>(Arrays.asList(packages)))
            .build();
        ModuleReference reference = new ModuleReference(descriptor, null) {
            @Override
            public ModuleReader open() {
                return new ModuleReader() {
                    @Override
                    public Optional<URI> find(String name) {
                        return Optional.empty();
                    }

                    @Override
                    public Stream<String> list() {
                        return Stream.empty();
                    }

                    @Override
                    public void close() {
                    }
                };
            }
        };
        return new ModuleFinder() {
            @Override
            public Optional<ModuleReference> find(String name) {
                return name.equals(moduleName) ? Optional.of(reference) : Optional.empty();
            }

            @Override
            public Set<ModuleReference> findAll() {
                return Collections.singleton(reference);
            }
        };
    }
}
