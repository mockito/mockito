/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.ModuleLayer;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.ModuleVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.utility.OpenedClassReader;
import org.junit.Test;
import org.mockito.Mockito;

public class CanLoadWithSimpleModule {

    @Test
    public void can_open_simple_module() throws Exception {
        Path jar = modularJar(true, true, true);
        ModuleLayer layer = layer(jar);

        Class<?> type = layer.findLoader("mockito.test").loadClass("sample.MyCallable");
        @SuppressWarnings("unchecked")
        Callable<String> mock = (Callable<String>) Mockito.mock(type);
        Mockito.when(mock.call()).thenCallRealMethod();
        Object result = mock.call();

        // We should have a check that fails mock creation of mock cannot be loaded in target class loader.
        assertThat(mock.getClass().getName()).startsWith("org.mockito.codegen.MyCallable$MockitoMock$");
        assertThat(result).isEqualTo("foo");
    }

    private static Path modularJar(boolean publik, boolean exported, boolean opened) throws IOException {
        Path jar = Files.createTempFile("sample-module", ".jar");
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jar))) {
            out.putNextEntry(new JarEntry("module-info.class"));
            out.write(moduleInfo(exported, opened));
            out.closeEntry();
            out.putNextEntry(new JarEntry("sample/MyCallable.class"));
            out.write(type(publik));
            out.closeEntry();
        }
        return jar;
    }

    private static byte[] type(boolean publik) {
        return new ByteBuddy()
            .subclass(Callable.class)
            .name("sample.MyCallable")
            .merge(publik ? Visibility.PUBLIC : Visibility.PACKAGE_PRIVATE)
            .method(named("call"))
            .intercept(FixedValue.value("foo"))
            .make()
            .getBytes();
    }

    private static byte[] moduleInfo(boolean exported, boolean opened) {
        ClassWriter classWriter = new ClassWriter(OpenedClassReader.ASM_API);
        classWriter.visit(Opcodes.V9, Opcodes.ACC_MODULE, "module-info", null, null, null);
        ModuleVisitor mv = classWriter.visitModule("mockito.test", 0, null);
        mv.visitRequire("java.base", Opcodes.ACC_MANDATED, null);
        mv.visitPackage("sample");
        if (exported) {
            mv.visitExport("sample", 0);
        }
        if (opened) {
            mv.visitOpen("sample", 0);
        }
        mv.visitEnd();
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private static ModuleLayer layer(Path jar) {
        Configuration configuration = Configuration.resolve(
            ModuleFinder.of(jar),
            Collections.singletonList(ModuleLayer.boot().configuration()),
            ModuleFinder.of(),
            Collections.singleton("mockito.test")
        );

        return ModuleLayer.defineModulesWithOneLoader(
            configuration,
            Collections.singletonList(ModuleLayer.boot()),
            null
        ).layer();
    }

}
