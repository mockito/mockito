/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.ModuleVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.OpenedClassReader;
import org.junit.Test;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;

public class CanLoadWithSimpleModule {

    @Test
    public void can_define_class_in_open_module() throws Exception {
        Path jar = modularJar(true, true, true);
        ModuleLayer layer = layer(jar);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> mockito = loader.loadClass("org.mockito.Mockito");
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) mockito.getMethod("mock", Class.class).invoke(null, type);
            Object stubbing = mockito.getMethod("when", Object.class).invoke(null, mock.call());
            loader.loadClass("org.mockito.stubbing.OngoingStubbing").getMethod("thenCallRealMethod").invoke(stubbing);

            assertThat(mock.getClass().getName()).startsWith("sample.MyCallable$MockitoMock$");
            assertThat(mock.call()).isEqualTo("foo");
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

    private static ModuleLayer layer(Path jar) throws MalformedURLException {
        Configuration configuration = Configuration.resolve(
            ModuleFinder.of(jar),
            Collections.singletonList(ModuleLayer.boot().configuration()),
            ModuleFinder.of(),
            Collections.singleton("mockito.test")
        );

        ClassLoader classLoader = new ReplicatingClassLoader(jar);
        ModuleLayer.Controller controller = ModuleLayer.defineModules(
            configuration,
            Collections.singletonList(ModuleLayer.boot()),
            module -> classLoader
        );
        // TODO: Add logic to Mockito that adds this read automatically if possible.
        controller.addReads(
            controller.layer().findModule("mockito.test").orElseThrow(IllegalStateException::new),
            controller.layer().findLoader("mockito.test").getUnnamedModule()
        );
        return controller.layer();
    }

}
