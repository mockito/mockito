/*
 * Copyright (c) 2020 Mockito contributors
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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.module.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ModuleUtil {

    public static Path modularJar(boolean isPublic, boolean isExported, boolean isOpened) throws IOException {
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

    public static ModuleLayer layer(Path jar, boolean canRead, boolean namedModules) throws MalformedURLException {
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
